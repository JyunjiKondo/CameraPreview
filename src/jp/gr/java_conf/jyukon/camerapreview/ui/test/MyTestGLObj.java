
package jp.gr.java_conf.jyukon.camerapreview.ui.test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class MyTestGLObj {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBufferN;
    private ByteBuffer mIndexBufferE;
    private ByteBuffer mIndexBufferS;
    private ByteBuffer mIndexBufferW;
    private ByteBuffer mIndexBufferT;
    private ByteBuffer mIndexBufferB;

    public MyTestGLObj(GL10 gl) {
        float vertices[] = {
                0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0, 1, 1, 0, 1, 2, 0, 1, 0, 0, 2, 1, 0, 2, 2, 0, 2
        };

        float colors[] = {
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
        };

        byte indicesN[] = { // GL_LINES
                // N
                0, 6, 6, 2, 2, 8
        };

        byte indicesE[] = { // GL_LINES
                // E
                0, 2, 3, 5, 6, 8, 0, 6
        };

        byte indicesS[] = { // GL_LINES
                // S
                0, 2, 2, 5, 5, 3, 3, 6, 6, 8
        };

        byte indicesW[] = { // GL_LINES
                // W
                0, 6, 1, 7, 2, 8, 0, 2
        };

        byte indicesT[] = { // GL_LINES
                // T
                1, 7, 6, 8
        };

        byte indicesB[] = { // GL_LINES
                // B
                0, 2, 3, 5, 6, 8, 0, 6, 2, 8
        };

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        ByteBuffer vbb;
        vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        vbb = ByteBuffer.allocateDirect(colors.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mColorBuffer = vbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBufferN = ByteBuffer.allocateDirect(indicesN.length);
        mIndexBufferN.put(indicesN);
        mIndexBufferN.position(0);

        mIndexBufferE = ByteBuffer.allocateDirect(indicesE.length);
        mIndexBufferE.put(indicesE);
        mIndexBufferE.position(0);

        mIndexBufferS = ByteBuffer.allocateDirect(indicesS.length);
        mIndexBufferS.put(indicesS);
        mIndexBufferS.position(0);

        mIndexBufferW = ByteBuffer.allocateDirect(indicesW.length);
        mIndexBufferW.put(indicesW);
        mIndexBufferW.position(0);

        mIndexBufferT = ByteBuffer.allocateDirect(indicesT.length);
        mIndexBufferT.put(indicesT);
        mIndexBufferT.position(0);

        mIndexBufferB = ByteBuffer.allocateDirect(indicesB.length);
        mIndexBufferB.put(indicesB);
        mIndexBufferB.position(0);
    }

    public void draw(GL10 gl) {
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glLineWidth(10.0f);

        gl.glPushMatrix();
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBufferN);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(-90, 0, 0, 1);
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 8, GL10.GL_UNSIGNED_BYTE, mIndexBufferE);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(-180, 0, 0, 1);
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 10, GL10.GL_UNSIGNED_BYTE, mIndexBufferS);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90, 0, 0, 1);
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 8, GL10.GL_UNSIGNED_BYTE, mIndexBufferW);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 4, GL10.GL_UNSIGNED_BYTE, mIndexBufferT);
        gl.glPopMatrix();

        gl.glPushMatrix();
        gl.glRotatef(-90, 1, 0, 0);
        gl.glTranslatef(-1, 10, -1);
        gl.glDrawElements(GL10.GL_LINES, 10, GL10.GL_UNSIGNED_BYTE, mIndexBufferB);
        gl.glPopMatrix();
    }

}
