package com.mehdi.heavyequipment;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Game3DView extends GLSurfaceView {

    private final Renderer renderer;

    public Game3DView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new Renderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private static class Renderer implements GLSurfaceView.Renderer {

        private final float[] projectionMatrix = new float[16];
        private final float[] viewMatrix = new float[16];
        private final float[] modelMatrix = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / Math.max(height, 1);

            Matrix.frustumM(
                    projectionMatrix,
                    0,
                    -ratio,
                    ratio,
                    -1,
                    1,
                    3,
                    100
            );
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(
                    GLES20.GL_COLOR_BUFFER_BIT |
                    GLES20.GL_DEPTH_BUFFER_BIT
            );

            Matrix.setLookAtM(
                    viewMatrix,
                    0,
                    0f, 0f, 6f,
                    0f, 0f, 0f,
                    0f, 1f, 0f
            );

            Matrix.setIdentityM(modelMatrix, 0);
        }
    }
}
