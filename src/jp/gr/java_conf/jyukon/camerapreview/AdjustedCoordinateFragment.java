
package jp.gr.java_conf.jyukon.camerapreview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Surface;

public class AdjustedCoordinateFragment extends CompassFragment {
    private static final String TAG = AdjustedCoordinateFragment.class.getSimpleName();
    private float[] outR = new float[16];

    @Override
    protected void startSensor() {
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(myEventListener, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(myEventListener, magneticField,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
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

            int x = SensorManager.AXIS_X;
            int y = SensorManager.AXIS_Y;

            switch (mRotation) {
                case Surface.ROTATION_0:
                    break;
                case Surface.ROTATION_90:
                    x = SensorManager.AXIS_Y;
                    y = SensorManager.AXIS_MINUS_X;
                    break;
                case Surface.ROTATION_180:
                    x = SensorManager.AXIS_MINUS_X;
                    y = SensorManager.AXIS_MINUS_Y;
                    break;
                case Surface.ROTATION_270:
                    x = SensorManager.AXIS_MINUS_Y;
                    y = SensorManager.AXIS_X;
                    break;
            }
            SensorManager.remapCoordinateSystem(mR, x, y, outR);
            mGLView.setR(outR);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
