package com.mehdi.heavyequipment;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

public class Game3DView extends GLSurfaceView {

    public Game3DView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new Renderer3D());
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private static class Renderer3D implements GLSurfaceView.Renderer {

        private final float[] projection = new float[16];
        private final float[] view = new float[16];
        private final float[] model = new float[16];
        private final float[] mvp = new float[16];

        @Override
        public void onSurfaceCreated(
                javax.microedition.khronos.egl.EGLConfig config) {

            GLES20.glClearColor(0.5f, 0.7f, 1.0f, 1.0f);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(
                javax.microedition.khronos.opengles.GL10 gl,
                int width,
                int height) {

            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            Matrix.frustumM(
                    projection
