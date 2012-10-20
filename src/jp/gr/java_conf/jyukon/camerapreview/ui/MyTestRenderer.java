
package jp.gr.java_conf.jyukon.camerapreview.ui;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.gr.java_conf.jyukon.camerapreview.ui.test.MyTestGLObj;

public class MyTestRenderer extends AbstractRenderer {
    MyTestGLObj mMyTestGLObj;

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        mMyTestGLObj.draw(gl);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        mMyTestGLObj = new MyTestGLObj(gl);
    }

}
