
package jp.gr.java_conf.jyukon.camerapreview.ui;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLView extends GLSurfaceView {
    AbstractRenderer mRenderer;

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mRenderer = new MyTestRenderer();
        setRenderer(mRenderer);
    }

    public void setR(float[] R) {
        mRenderer.setR(R);
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }
}
