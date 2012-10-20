
package jp.gr.java_conf.jyukon.camerapreview;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Surface;

public class AdjustedOrientationFragment extends CameraPreviewFragment {
    private static final String TAG = AdjustedOrientationFragment.class.getSimpleName();

    protected int mRotation;
    protected Boolean mPreviewRotated;

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        setCameraDisplayOrientation();
    }

    public void setCameraDisplayOrientation() {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        mRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (mRotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
        mPreviewRotated = (result == 90 || result == 270) ? true : false;
    }
}
