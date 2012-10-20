
package jp.gr.java_conf.jyukon.camerapreview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

public class CompassUsingRotationVectorFragment extends CompassFragment {
    private static final String TAG = CompassUsingRotationVectorFragment.class.getSimpleName();
    private float[] outR = new float[16];

    @Override
    protected void startSensor() {
        Sensor rotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        // initialize the rotation matrix to identity
        mR = new float[16];
        mR[0] = 1;
        mR[4] = 1;
        mR[8] = 1;
        mR[12] = 1;
        mSensorManager.registerListener(myEventListener, rotationVector,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void stopSensor() {
        mSensorManager.unregisterListener(myEventListener);
    }

    private final SensorEventListener myEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(mR, event.values);

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
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
}
