package com.example.gymcompanion.ui.exercise;

import static java.lang.Math.atan2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import java.util.Collections;
import java.util.Objects;

public class ExercisePageActivityLiveFeed extends AppCompatActivity {

    public static final int PERMISSION_CODE = 143;
    private CameraCharacteristics characteristics;
    private CameraManager cameraManager;
    private Handler handler;
    private String cameraID;
    private CameraDevice mCameraDevice;
    private TextureView textureView;
    private PoseDetector poseDetector;
    private int count = 1;
    private boolean onHold = true;
    private TextView counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);

        textureView = findViewById(R.id.textureView);
        counter = findViewById(R.id.counter);


        HandlerThread handlerThread = new HandlerThread("Video Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        cameraID = "" + CameraCharacteristics.LENS_FACING_BACK;
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);


        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                try {
                    requestPermission();
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
                surface.setDefaultBufferSize(width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                surface.release();
                mCameraDevice.close();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
                Bitmap bitmapImage = textureView.getBitmap();

                if (bitmapImage != null && textureView.isAvailable()) {
                    InputImage inputImage = InputImage.fromBitmap(textureView.getBitmap(), getDisplayRotation());
                    poseDetector.process(inputImage)
                            .addOnSuccessListener(pose -> {
//                                GraphicOverlay overlay = new GraphicOverlay(getApplicationContext());
//                                PoseGraphic poseGraphic = new PoseGraphic(overlay, pose, true, false, true);
//                                Paint paint = new Paint();
//                                paint.setStyle(Paint.Style.STROKE);
//                                paint.setColor(getColor(R.color.black));
//                                paint.setStrokeWidth(5);
//                                paint.setTextSize(30);
//
//                                Canvas canvas = new Canvas(bitmapImage);
//
//                                poseGraphic.draw(canvas);

                                PoseLandmark firstPoint = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
                                PoseLandmark midPoint = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
                                PoseLandmark lastPoint = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);

                                if (firstPoint != null && midPoint != null && lastPoint != null && firstPoint.getPosition().y < lastPoint.getPosition().y) {
                                    double result =
                                        Math.toDegrees(
                                                atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                                        lastPoint.getPosition().x - midPoint.getPosition().x)
                                                        - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                                        firstPoint.getPosition().x - midPoint.getPosition().x));
                                    result = Math.abs(result); // Angle should never be negative

                                    if (result > 180) {
                                        result = (360.0 - result); // Always get the acute representation of the angle
                                    }
                                    if (result < 90) {
                                        onHold = false;
                                    }
                                    if (result > 150 && !onHold) {
                                        String tempText = getString(R.string.count_0) + count++;
                                        counter.setText(tempText);
                                        onHold = true;
                                    }

                                }

//                                imageView.setImageMatrix(setTextureTransform(characteristics));
//                                imageView.setImageBitmap(bitmapImage);

                            }).
                            addOnFailureListener(e -> Toast.makeText(ExercisePageActivityLiveFeed.this, "Sorry but " + e.getLocalizedMessage() + ". Please Try again later!", Toast.LENGTH_SHORT).show());
                }

            }
        });

    }

    private Matrix setTextureTransform(CameraCharacteristics characteristics) {
        Size previewSize = getPreviewSize(characteristics);
        int width = previewSize.getWidth();
        int height = previewSize.getHeight();
        int sensorOrientation = getCameraSensorOrientation(characteristics);

        // Indicate the size of the buffer the texture should expect
        Objects.requireNonNull(textureView.getSurfaceTexture()).setDefaultBufferSize(width, height);

        // Save the texture dimensions in a rectangle
        RectF viewRect = new RectF(0,0, textureView.getWidth(), textureView.getHeight());
        // Determine the rotation of the display
        float rotationDegrees = 0;
        try {
            rotationDegrees = (float)getDisplayRotation();
        } catch (Exception ignored) {
        }
        float w, h;
        if ((sensorOrientation - rotationDegrees) % 180 == 0) {
            w = width;
            h = height;
        } else {
            // Swap the width and height if the sensor orientation and display rotation don't match
            w = height;
            h = width;
        }
        float viewAspectRatio = viewRect.width()/viewRect.height();
        float imageAspectRatio = w/h;
        final PointF scale;
        // This will make the camera frame fill the texture view, if you'd like to fit it into the view swap the "<" sign for ">"
        if (viewAspectRatio < imageAspectRatio) {
            // If the view is "thinner" than the image constrain the height and calculate the scale for the texture width
            scale = new PointF((viewRect.height() / viewRect.width()) * ((float) height / (float) width), 1f);
        } else {
            scale = new PointF(1f, (viewRect.width() / viewRect.height()) * ((float) width / (float) height));
        }
        if (rotationDegrees % 180 != 0) {
            // If we need to rotate the texture 90º we need to adjust the scale
            float multiplier = viewAspectRatio < imageAspectRatio ? w/h : h/w;
            scale.x *= multiplier;
            scale.y *= multiplier;
        }

        Matrix matrix = new Matrix();
        // Set the scale
        matrix.setScale(scale.x, scale.y, viewRect.centerX(), viewRect.centerY());
        if (rotationDegrees != 0) {
            // Set rotation of the device isn't upright
            matrix.postRotate(0 - rotationDegrees, viewRect.centerX(), viewRect.centerY());
        }
        // Transform the texture
        return matrix;
    }

    int getDisplayRotation() {
        switch (textureView.getDisplay().getRotation()) {

            case Surface.ROTATION_90:
                return  90;

            case Surface.ROTATION_180:
                return  180;

            case Surface.ROTATION_270:
                return 270;

            default:
                return 0;
        }
    }

    Size getPreviewSize(CameraCharacteristics characteristics) {
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size[] previewSizes = Objects.requireNonNull(map).getOutputSizes(SurfaceTexture.class);
        // TODO: decide on which size fits your view size the best
        return previewSizes[0];
    }

    int getCameraSensorOrientation(CameraCharacteristics characteristics) {
        Integer cameraOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        return (360 - (cameraOrientation != null ? cameraOrientation : 0)) % 360;
    }

    @SuppressLint("MissingPermission")
    private void openCamera() throws CameraAccessException {
        cameraManager.openCamera(cameraID, new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                mCameraDevice = cameraDevice;
                Surface previewSurface = new Surface(textureView.getSurfaceTexture());
                try {
                    try {
                        characteristics = cameraManager.getCameraCharacteristics(cameraID);
                        textureView.setTransform(setTextureTransform(characteristics));
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                    CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    captureBuilder.addTarget(previewSurface);

                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).addTarget(previewSurface);
                    mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface), new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            try {
                                cameraCaptureSession.setRepeatingRequest(captureBuilder.build(), null, null);
                            } catch (CameraAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                            mCameraDevice.close();
                            mCameraDevice = null;
                        }
                    }, handler);

                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }


            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }, handler);
    }


    private void requestPermission() throws CameraAccessException {

        if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            openCamera();
        }
        else {

            // Request permission to the user
            ActivityCompat.requestPermissions(ExercisePageActivityLiveFeed.this,
                    new String[] {
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    PERMISSION_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    openCamera();
                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                Toast.makeText(this, "sorry but you have to grant access to use this feature", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCameraDevice.close();
        try {
            openCamera();
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mCameraDevice.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraDevice.close();
    }
}