
package jp.gr.java_conf.jyukon.camerapreview;

import java.util.List;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;

public class AdjustedAspectRatioFragment extends AdjustedOrientationFragment {
    private static final String TAG = AdjustedAspectRatioFragment.class.getSimpleName();
    private List<Size> mSupportedPreviewSizes;
    private Size mPreviewSize;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        final SurfaceView surfaceView = (SurfaceView) getView().findViewById(R.id.surfaceView);
        ViewTreeObserver viewTreeObserver = surfaceView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                int width = surfaceView.getWidth();
                int height = surfaceView.getHeight();
                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                Log.i(TAG, String.format("%s W:%d H:%d R:%d", TAG, width, height, rotation));
                if (adjustLayout(surfaceView, width, height)) {
                    surfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, String.format("surfaceChanged(, %d, %d, %d)", format, width, height));
        super.surfaceChanged(holder, format, width, height);

        int w, h;

        if (mPreviewRotated) {
            w = height;
            h = width;
        } else {
            w = width;
            h = height;
        }
        mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, w, h);
        Log.i(TAG, String.format("W:%d H:%d R:%d PR: %s PW:%d PH:%d", width, height, mRotation,
                mPreviewRotated ? "true" : "false", mPreviewSize.width, mPreviewSize.height));

        mCamera.stopPreview();
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);

        mCamera.setParameters(parameters);
        Log.i(TAG,
                String.format("getParameters %d %d", parameters.getPreviewSize().width,
                        parameters.getPreviewSize().height));
        mCamera.startPreview();
    }

    private Boolean adjustLayout(View view, int width, int height) {
        int previewWidth = width;
        int previewHeight = height;
        if (mPreviewSize != null) {
            previewWidth = mPreviewSize.width;
            previewHeight = mPreviewSize.height;
        } else {
            return false;
        }

        mCamera.stopPreview();

        if (width * previewHeight > height * previewWidth) {
            final int scaledChildWidth = previewWidth * height / previewHeight;
            view.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2, height);
            Log.i(TAG, String.format("adjustLayout#1 %d %d %d %d", (width - scaledChildWidth) / 2,
                    0, (width + scaledChildWidth) / 2, height));
        } else {
            final int scaledChildHeight = previewHeight * width / previewWidth;
            view.layout(0, (height - scaledChildHeight) / 2, width,
                    (height + scaledChildHeight) / 2);
            Log.i(TAG, String.format("adjustLayout#2 %d %d %d %d", 0,
                    (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2));
        }
        return true;
    }

    private Size getOptimalPreviewSize(List<Size> supportedPreviewSizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) width / height;
        if (supportedPreviewSizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = height;

        // Try to find an size match aspect ratio and size
        for (Size size : supportedPreviewSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : supportedPreviewSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
