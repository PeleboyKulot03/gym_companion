package com.example.gymcompanion.ui.Exercise.decoders;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.view.Surface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CodecOutputSurface implements SurfaceTexture.OnFrameAvailableListener{
    private SurfaceTextureRender mTextureRender;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;

    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;
    private int mWidth = 0;
    private int mHeight = 0;

    private ReentrantLock mFrameSyncLock = new ReentrantLock();
    private Condition condition = mFrameSyncLock.newCondition();

    private boolean mFrameAvailable = false;

    private ByteBuffer mPixelBuf;

    private boolean verbose = true;

    private boolean isPortrait;
    public CodecOutputSurface(int width, int height, boolean isPortrait) {
        mWidth = width;
        mHeight = height;
        this.isPortrait = isPortrait;

        eglSetup();
        makeCurrent();

        // Creates interconnected instances of SurfaceTextureRender, SurfaceTexture, and Surface.
        mTextureRender = new SurfaceTextureRender();
        mTextureRender.surfaceCreated();

        mSurfaceTexture = new SurfaceTexture(mTextureRender.getTextureId());

        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurface = new Surface(mSurfaceTexture);
        mPixelBuf = ByteBuffer.allocateDirect(mWidth * mHeight * 4);
        mPixelBuf.order(ByteOrder.LITTLE_ENDIAN);
    }

    private void eglSetup() {
        mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
            throw new RuntimeException("unable to get EGL14 display");
        }
        int[] version = new int[2];
        if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
            mEGLDisplay = null;
            throw new RuntimeException("unable to initialize EGL14");
        }

        // Configure EGL for pbuffer and OpenGL ES 2.0, 24-bit RGB.
        int[] attributeList = {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                EGL14.EGL_NONE
        };

        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];

        if (!EGL14.eglChooseConfig(mEGLDisplay, attributeList, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
        }

        // Configure context for OpenGL ES 2.0.
        int[] contextSpec = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };

        mEGLContext = EGL14.eglCreateContext(
                mEGLDisplay, configs[0], EGL14.EGL_NO_CONTEXT,
                contextSpec, 0
        );
        checkEglError("eglCreateContext");
        if (mEGLContext == null) {
            throw new RuntimeException("null context");
        }

        // Create a PBuffer surface.
        int[] surfaceAttribs = {
                EGL14.EGL_WIDTH, mWidth,
                EGL14.EGL_HEIGHT, mHeight,
                EGL14.EGL_NONE
        };

        mEGLSurface = EGL14.eglCreatePbufferSurface(mEGLDisplay, configs[0], surfaceAttribs, 0);
        checkEglError("eglCreatePBufferSurface");
        if (mEGLSurface == null) {
            throw new RuntimeException("Surface was null");
        }
    }

    /**
     * Discard all resources held by this class, notably the EGL context.
     */
    public void release() {
        if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
            EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(mEGLDisplay);
        }
        mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        mEGLContext = EGL14.EGL_NO_CONTEXT;
        mEGLSurface = EGL14.EGL_NO_SURFACE;
        mSurface.release();

        // this causes a bunch of warnings that appear harmless but might confuse someone:
        //  W BufferQueue: [unnamed-3997-2] cancelBuffer: BufferQueue has been abandoned!
        //mSurfaceTexture.release();
        mTextureRender = null;
        mSurface = null;
        mSurfaceTexture = null;
    }

    /**
     * Makes our EGL context and surface current.
     */
    private void makeCurrent() {
        if (!EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    /**
     * Returns the Surface.
     */
    public Surface getSurface() {
        return mSurface;
    }

    /**
     * Latches the next buffer into the texture.  Must be called from the thread that created
     * the CodecOutputSurface object.  (More specifically, it must be called on the thread
     * with the EGLContext that contains the GL texture object used by SurfaceTexture.)
     */
    public void awaitNewImage() {
        int timeoutMS = 2500;
        mFrameSyncLock.lock();
        try {
            while (!mFrameAvailable) {
                try {
                    // Wait for onFrameAvailable() to signal us.  Use a timeout to avoid
                    // stalling the test if it doesn't arrive.
                    condition.await(timeoutMS, TimeUnit.MILLISECONDS);
                    if (!mFrameAvailable) {
                        // TODO: if "spurious wakeup", continue while loop
                        throw new RuntimeException("Frame wait timed out");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            mFrameAvailable = false;
            mFrameSyncLock.unlock();
        }

        // Latch the data.
        mTextureRender.checkGlError("before updateTexImage");
        mSurfaceTexture.updateTexImage();
    }


    public void drawImage(boolean invert) {
        if (mSurfaceTexture != null) {
            mTextureRender.drawFrame(mSurfaceTexture, invert);
        }
    }


    // SurfaceTexture callback

    /**
     * Get the current frame by glReadPixels
     */
    Frame retrieveFrame(int framePos, long timestamp) {
        mPixelBuf.rewind();
        GLES20.glReadPixels(0, 0, mWidth, mHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mPixelBuf);

        int rotation = 0;
        boolean isFlipX = false;
        if (isPortrait){
            rotation = 180;
            isFlipX = true;
        }

        return new Frame(mPixelBuf, mWidth, mHeight, framePos, timestamp, rotation, isFlipX, false);
    }

    /**
     * Checks for EGL errors.
     */
    private void checkEglError(String msg) {
        if (EGL14.eglGetError() != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + ": EGL error: 0x");
        }
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        if (verbose) {
            mFrameSyncLock.lock();

            try {
                if (mFrameAvailable) {
                    throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
                }
                mFrameAvailable = true;
                condition.signalAll();
            } finally {
                mFrameSyncLock.unlock();
            }

        }
    }
}
