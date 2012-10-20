package jp.gr.java_conf.jyukon.camerapreview;

public class AdaptiveFilter {
    private static final float kAccelerometerMinStep = 0.02f;
    private static final float kAccelerometerNoiseAttenuation = 3.0f;
    private static final float kUpdateFrequency = 30.0f;
    private static final float kCutoffFrequency = 5.0f;
    private static double dt = 1.0 / kUpdateFrequency;
    private static double RC = 1.0 / kCutoffFrequency;
    private static double kFilterConstant = dt / (dt + RC);

    static float[] apply(float[] sensorValues, float[] newValues) {
        float[] values = new float[3];
        if (sensorValues == null) {
            return newValues;
        }
        double d = Clamp(
                Math.abs(Norm(sensorValues[0], sensorValues[1], sensorValues[2])
                        - Norm(newValues[0], newValues[1], newValues[2]))
                        / kAccelerometerMinStep - 1.0, 0.0, 1.0);
        double alpha = (1.0 - d) * kFilterConstant / kAccelerometerNoiseAttenuation + d
                * kFilterConstant;
        for (int i = 0; i < 3; i++) {
            values[i] = (float) (newValues[i] * alpha + sensorValues[i] * (1.0f - alpha));
        }
        return values;
    }

    static double Clamp(double v, double min, double max) {
        return (v > max) ? max : ((v < min) ? min : v);
    }

    static double Norm(double x, double y, double z) {
        return Math.sqrt(x * x + y * y + z * z);
    }
}
