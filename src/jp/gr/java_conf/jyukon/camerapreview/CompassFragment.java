
package jp.gr.java_conf.jyukon.camerapreview;

import jp.gr.java_conf.jyukon.camerapreview.ui.GLView;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CompassFragment extends AdjustedAspectRatioFragment {
    private static final String TAG = CompassFragment.class.getSimpleName();
    protected GLView mGLView;
    protected SensorManager mSensorManager;
    protected float[] mR = new float[16];

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        return inflater.inflate(R.layout.camera_preview_with_gl_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mGLView = (GLView) getView().findViewById(R.id.GLView);
        mGLView.setZOrderMediaOverlay(true);
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

        startSensor();
        mGLView.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();

        stopSensor();
        mGLView.onPause();
    }

    protected void startSensor() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(myEventListener, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(myEventListener, magneticField,
                SensorManager.SENSOR_DELAY_UI);
    }

    protected void stopSensor() {
        mSensorManager.unregisterListener(myEventListener);
    }

    private final SensorEventListener myEventListener = new SensorEventListener() {
        private float[] accelerometerValues;
        private float[] geomagneticMatrix;

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accelerometerValues = AdaptiveFilter.apply(accelerometerValues,
                            event.values.clone());
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    geomagneticMatrix = AdaptiveFilter.apply(geomagneticMatrix,
                            event.values.clone());
                    break;
                default:
                    return;
            }

            if (accelerometerValues == null || geomagneticMatrix == null) {
                Log.i(TAG, "no sensor values");
                return;
            }
            SensorManager.getRotationMatrix(mR, null, accelerometerValues, geomagneticMatrix);
            mGLView.setR(mR);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
