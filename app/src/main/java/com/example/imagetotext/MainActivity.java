package com.example.imagetotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    /**
     * The CameraManager object used to access the camera.
     */
    private CameraManager manager;
    /**
     * an int representing the direction the camera is facing, used to find the rear camera.
     */
    private int cameraFacing;
    /**
     * the string key representing the camera we want to connect to.
     */
    private String cameraId;
    /**
     * A listener to catch changes to our TextureView.
     */
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    /**
     * Another thread to handle background tasks.
     */
    private HandlerThread backgroundThread;
    private Handler backgroundHandler;
    private CameraDevice.StateCallback stateCallback;
    private Size previewSize;
    /**
     * The cameraDevice that we connected to.
     */
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest captureRequest;

    private TextureView textureView;
    private Button captureButton;

    public Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Creating");
        textureView = findViewById(R.id.texture_view);
        captureButton = findViewById(R.id.button_capture);

        int CAMERA_REQUEST_CODE = 1510;

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                CAMERA_REQUEST_CODE);

        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraFacing = CameraCharacteristics.LENS_FACING_BACK;

        //setup listener on the surfaceTexture of the TextureView
        //setup and connect to camera when the surfaceTexture is ready
        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width,
                                                  int height) {
                setUpCamera();
                openCamera();
            }
            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width,
                                                    int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        };

        // create a listener on the camera
        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice cameraDevice) {
                // if camera is successfully opened, store the cameraDevice to this activities scope
                MainActivity.this.cameraDevice = cameraDevice;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                // cleanup after disconnected
                cameraDevice.close();
                MainActivity.this.cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice cameraDevice, int error) {
                // cleanup on error
                cameraDevice.close();
                MainActivity.this.cameraDevice = null;
            }
        };
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                capturedImage = textureView.getBitmap();
                Intent areaSelect = new Intent(MainActivity.this, Image_BoxEditor.class);
                startActivity(areaSelect);
            }
        });

    }

    /**
     * set the surfaceTextureListener
     * invoke the backgroundThread
     */
    @Override
    protected void onResume() {
        super.onResume();
        openBackgroundThread();
        // check to see if textureView is already available
        if (textureView.isAvailable()) {
            setUpCamera();
            openCamera();
        } else {
            // if it isn't set the listener to catch it when it is
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // cleanup cameraDevice and backgroundThread to prevent memory leakage / conflicts.
        closeCamera();
        closeBackgroundThread();
    }

    /**
     * closes camera related connections and deletes references to them.
     */
    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    /**
     * closes the backgroundThread and clears the related references.
     */
    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }

    /**
     * Find the cameraDevice which is facing backwards on the device and set its id to cameraId.
     * Sets the preview size to match the maximum resolution output of the cameraDevice
     */
    private void setUpCamera() {
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics =
                        manager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) ==
                        cameraFacing) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    this.cameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * try to open the cameraDevice specified by cameraId.
     */
    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                manager.openCamera(cameraId, stateCallback, backgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * background thread to prevent overloading the main thread.
     */
    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            // set the surfaceTexture to the appropriate size
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            final CaptureRequest.Builder captureRequestBuilder =
                    cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                MainActivity.this.cameraCaptureSession = cameraCaptureSession;
                                MainActivity.this.cameraCaptureSession.setRepeatingRequest(captureRequest,
                                        null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
