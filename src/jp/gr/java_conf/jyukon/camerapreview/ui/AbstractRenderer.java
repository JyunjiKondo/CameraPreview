
package jp.gr.java_conf.jyukon.camerapreview.ui;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public abstract class AbstractRenderer implements Renderer {
    private float[] mR;

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        if (mR != null) {
            gl.glMultMatrixf(mR, 0);
        }
    }

    private static final float kGLMETER = 20.0f;
    private static final float kNearClip = 3.0f; // 15cm
    private static final float kFarClip = 310.0f * kGLMETER; // 300m + reserve
                                                             // (to avoid
                                                             // flashing)
    private static final float kFieldOfView = 37.0f; // degrees
    protected float nearClip;
    protected float nearClipWidth;
    protected float nearClipHeight;

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float size = kNearClip * (float) Math.tan((kFieldOfView / 2.0f) * Math.PI / 180.0f);
        float aspect = (float) width / (float) height;
        nearClip = kNearClip;

        float frustumRight = (width < height) ? size : size * aspect;
        float frustumLeft = -frustumRight;
        float frustumTop = frustumRight / aspect;
        float frustumBottom = -frustumTop;
        nearClipWidth = frustumRight * 2.0f;
        nearClipHeight = frustumTop * 2.0f;
        gl.glFrustumf(frustumLeft, frustumRight, frustumBottom, frustumTop, nearClip, kFarClip);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }

    public void setR(float[] R) {
        mR = R.clone();
    }
}
