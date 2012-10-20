
package jp.gr.java_conf.jyukon.camerapreview;

import java.io.IOException;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class CameraPreviewFragment extends Fragment implements SurfaceHolder.Callback {
    private static final String TAG = CameraPreviewFragment.class.getSimpleName();
    protected Camera mCamera;
    protected int mCameraId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int numberOfCameras = Camera.getNumberOfCameras();
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.camera_preview_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        final SurfaceView surfaceView = (SurfaceView) getView().findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        // surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        ViewTreeObserver viewTreeObserver = surfaceView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = surfaceView.getWidth();
                int height = surfaceView.getHeight();
                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                Log.i(TAG, String.format("W:%d H:%d R:%d", width, height, rotation));
                surfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        mCamera = Camera.open(mCameraId);
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
        // called when power button pressed.
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        super.onDestroyView();
        // called when display rotation changed.
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, String.format("surfaceChanged(, %d, %d, %d)", format, width, height));
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException exception) {
            Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }
}
