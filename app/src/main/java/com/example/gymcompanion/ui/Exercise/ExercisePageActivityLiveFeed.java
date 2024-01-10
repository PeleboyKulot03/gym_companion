package com.example.gymcompanion.ui.Exercise;

import static java.lang.Math.abs;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.gymcompanion.R;
import com.example.gymcompanion.ui.Exercise.Logics.BarbellCurls;
import com.example.gymcompanion.ui.Exercise.Logics.BarbellRows;
import com.example.gymcompanion.ui.Exercise.Logics.Deadlift;
import com.example.gymcompanion.ui.Exercise.Logics.DumbbellShoulderPress;
import com.example.gymcompanion.ui.Exercise.Logics.FlatBenchPress;
import com.example.gymcompanion.ui.Exercise.Logics.SideLateralRaises;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import java.util.Collections;
import java.util.Objects;

public class ExercisePageActivityLiveFeed extends AppCompatActivity implements ILiveFeed{

    public static final int PERMISSION_CODE = 143;
    private CameraCharacteristics characteristics;
    private CameraManager cameraManager;
    private Handler handler;
    private String cameraID;
    private CameraDevice mCameraDevice;
    private TextureView textureView;
    private PoseDetector poseDetector;
    private ImageView imageView;
    private String exercise;
    private TextView timer;
    private TextView countDownTV;
    private int setNumber;
    private final Handler timerHandler = new Handler();
    private final Handler countDownHandler = new Handler();
    private int countDown = 3;
    private Canvas canvas;
    private Paint paint;
    int width;
    int height;
    boolean firstBox = true;
    private DumbbellShoulderPress dumbbellShoulderPressLogic;
    private SideLateralRaises sideLateralRaises;
    private FlatBenchPress flatBenchPress;
    private Deadlift deadlift;
    private BarbellRows barbellRows;
    private BarbellCurls barbellCurls;
    private final Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            countDownTV.setText(String.valueOf(countDown));
            countDown--;
            if (countDown == -1) {
                countDownTV.setVisibility(View.GONE);
                isAligned = true;
                isInTheBox = true;
                timer.setTextColor(getColor(R.color.black));
                timer.setText(getString(R.string.start_exercise));
                handler.removeCallbacks(this);
                return;
            }
            countDownHandler.postDelayed(this, 1000);
        }
    };
    private boolean isInTheBox = false;
    private boolean isAligned = false;
    private boolean isDone = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_page);
        Intent intent = getIntent();
        if (intent != null) {
            exercise = intent.getStringExtra("exercise");
            setNumber = intent.getIntExtra("sets", 3);
        }

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setTextSize(30);

        countDownTV = findViewById(R.id.countDownTV);
        textureView = findViewById(R.id.textureView);
        imageView = findViewById(R.id.imageView);
        TextView counter = findViewById(R.id.counter);
        timer = findViewById(R.id.timer);
        LiveFeedPresenter presenter = new LiveFeedPresenter(this);
        HandlerThread handlerThread = new HandlerThread("Video Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        cameraID = "" + CameraCharacteristics.LENS_FACING_BACK;
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        PoseDetectorOptions options = new PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build();

        poseDetector = PoseDetection.getClient(options);

        dumbbellShoulderPressLogic = new DumbbellShoulderPress(counter, timer, timerHandler, presenter, exercise, setNumber);
        sideLateralRaises = new SideLateralRaises(counter, timer, timerHandler, presenter, exercise, setNumber);
        flatBenchPress = new FlatBenchPress(counter, timer, timerHandler, presenter, exercise, setNumber);
        deadlift = new Deadlift(counter, timer, timerHandler, presenter, exercise, setNumber);
        barbellRows = new BarbellRows(counter, timer, timerHandler, presenter, exercise, setNumber);
        barbellCurls = new BarbellCurls(counter, timer, timerHandler, presenter, exercise, setNumber);

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
                width = textureView.getWidth();
                height = textureView.getHeight();
                Bitmap bitmapImage = textureView.getBitmap();
                if (bitmapImage != null && textureView.isAvailable() && isDone) {
                    InputImage inputImage = InputImage.fromBitmap(textureView.getBitmap(), getDisplayRotation());
                    poseDetector.process(inputImage)
                            .addOnSuccessListener(pose -> {
                                isDone = false;
                                paint.setColor(getColor(R.color.red));
                                GraphicOverlay overlay = new GraphicOverlay(getApplicationContext());
                                PoseGraphic poseGraphic = new PoseGraphic(overlay, pose, false, true, false);
                                canvas = new Canvas(bitmapImage);

                                // check if the position is eye level and the user is facing front
                                if (!isInTheBox) {
                                    if (!isAligned) {
                                        PoseLandmark leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE);
                                        PoseLandmark rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE);
                                        PoseLandmark lips = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH);
                                        PoseLandmark leftEars = pose.getPoseLandmark(PoseLandmark.LEFT_EAR);
                                        PoseLandmark rightEars = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR);
                                        PoseLandmark leftHands = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX);
                                        PoseLandmark rightHands = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX);
                                        PoseLandmark rightShoulders = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
                                        PoseLandmark leftShoulders = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);

                                        if (leftEye != null && rightEye != null && lips != null && leftEars != null && rightEars != null
                                                && leftHands != null && rightHands != null && leftShoulders != null && rightShoulders != null
                                                && leftHands.getPosition().x > 0 && rightHands.getPosition().x < width
                                                && leftShoulders.getPosition().x > 150 && rightShoulders.getPosition().x < width - 150) {
                                            Log.i("testing eyes", "left shoulder: " + leftShoulders.getPosition().x + "right shoulder: " + rightShoulders.getPosition().x);

                                            int faceWidth = (int) abs(leftEars.getPosition().x - rightEars.getPosition().x) / 2;
                                            int centerX = width / 2;
                                            int length = abs((int) ((leftEye.getPosition().y - 100) - (lips.getPosition().y + 100)));

                                            int leftPoint = centerX - faceWidth - 50;
                                            int topPoint = height / 4;
                                            int rightPoint = centerX + faceWidth + 50;
                                            int bottomPoint = topPoint + length;

                                            if (rightEye.getPosition().x - 40 > leftPoint
                                                    && leftEye.getPosition().x + 40 < rightPoint
                                                    && leftEye.getPosition().y - 50 > topPoint
                                                    && lips.getPosition().y + 50 < bottomPoint) {

                                                if (firstBox) {
                                                    countDownTV.setVisibility(View.VISIBLE);
                                                    countDownHandler.postDelayed(countDownRunnable, 0);
                                                    firstBox = false;
                                                }
                                                paint.setColor(getColor(R.color.green));
                                            }
                                            else {
                                                countDownTV.setVisibility(View.GONE);
                                                countDownHandler.removeCallbacks(countDownRunnable);
                                                countDown = 3;
                                                firstBox = true;
                                            }
                                            canvas.drawRect(
                                                    leftPoint,
                                                    topPoint,
                                                    rightPoint,
                                                    bottomPoint,
                                                    paint);
                                        }
                                        else {
                                            countDownTV.setVisibility(View.GONE);
                                            countDown = 3;
                                            countDownHandler.removeCallbacks(countDownRunnable);
                                        }
                                    }
                                    poseGraphic.draw(canvas, 0.0, 0.0, exercise, null);
                                    imageView.setImageMatrix(setTextureTransform(characteristics));
                                    imageView.setImageBitmap(bitmapImage);
                                    return;
                                }

                                // user is aligned within eye level and can now continue to do the exercise
                                chooseLogic(pose, poseGraphic);
                                imageView.setImageMatrix(setTextureTransform(characteristics));
                                imageView.setImageBitmap(bitmapImage);
                            })
                            .addOnCompleteListener(task -> {
                                if (task.isComplete()) {
                                    isDone = true;
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(ExercisePageActivityLiveFeed.this, "Sorry but " + e.getLocalizedMessage() + ". Please Try again later!", Toast.LENGTH_SHORT).show());
                }

            }
        });

    }

    private void chooseLogic(Pose pose, PoseGraphic poseGraphic) {
        if (exercise.equals("Dumbbell Shoulder Press")) {
            dumbbellShoulderPressLogic.getTheLandmarks(pose, poseGraphic, canvas);
            return;
        }
        if (exercise.equals("Side Lateral Raises")) {
            sideLateralRaises.getTheLandmarks(pose, poseGraphic, canvas);
        }
        if (exercise.equals("Flat Bench Press") || exercise.equals("Incline Bench Press")) {
            flatBenchPress.getTheLandmarks(pose, poseGraphic, canvas);
        }
        if (exercise.equals("Skull Crushers")) {
            flatBenchPress.getTheLandmarks(pose, poseGraphic, canvas);
        }

        if (exercise.equals("Deadlift")) {
            deadlift.getTheLandmarks(pose, poseGraphic, canvas);
        }
        if (exercise.equals("Barbell Rows")) {
            barbellRows.getTheLandmarks(pose, poseGraphic, canvas);
        }
        if (exercise.equals("Barbell Curls") || exercise.equals("Preacher Curls")) {
            barbellCurls.getTheLandmarks(pose, poseGraphic, canvas);
        }
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

    @Override
    public void onAddData(boolean verdict, String message) {

    }
}