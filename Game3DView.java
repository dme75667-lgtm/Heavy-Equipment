package com.mehdi.heavyequipment;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Game3DView extends GLSurfaceView {

    private final Renderer3D renderer;

    public Game3DView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new Renderer3D();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = event.getX() - renderer.lastX;
            float dy = event.getY() - renderer.lastY;

            renderer.cameraX += dx * 0.01f;
            renderer.cameraY += dy * 0.01f;

            renderer.cameraY =
                    Math.max(-2.0f, Math.min(4.0f, renderer.cameraY));

            renderer.lastX = event.getX();
            renderer.lastY = event.getY();

            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            renderer.lastX = event.getX();
            renderer.lastY = event.getY();
            return true;
        }

        return true;
    }

    private static class Renderer3D implements GLSurfaceView.Renderer {

        private final float[] projection = new float[16];
        private final float[] view = new float[16];
        private final float[] model = new float[16];
        private final float[] mvp = new float[16];

        private FloatBuffer cubeBuffer;
        private int program;

        private float cameraX = 0;
        private float cameraY = 1.5f;

        private float lastX;
        private float lastY;

        private final float[] cubeVertices = {

                // Front
                -1,-1, 1,
                 1,-1, 1,
                 1, 1, 1,
                -1, 1, 1,

                // Back
                -1,-1,-1,
                -1, 1,-1,
                 1, 1,-1,
                 1,-1,-1
        };

        @Override
        public void onSurfaceCreated(
                javax.microedition.khronos.egl.EGLConfig config) {

            GLES20.glClearColor(
                    0.45f,
                    0.65f,
                    0.90f,
                    1.0f
            );

            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            cubeBuffer = ByteBuffer
                    .allocateDirect(cubeVertices.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();

            cubeBuffer.put(cubeVertices);
            cubeBuffer.position(0);

            String vertexShader =
                    "attribute vec4 vPosition;" +
                    "uniform mat4 uMVP;" +
                    "void main() {" +
                    "gl_Position = uMVP * vPosition;" +
                    "}";

            String fragmentShader =
                    "precision mediump float;" +
                    "uniform vec4 uColor;" +
                    "void main() {" +
                    "gl_FragColor = uColor;" +
                    "}";

            int vertex = loadShader(
                    GLES20.GL_VERTEX_SHADER,
                    vertexShader
            );

            int fragment = loadShader(
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShader
            );

            program = GLES20.glCreateProgram();

            GLES20.glAttachShader(program, vertex);
            GLES20.glAttachShader(program, fragment);

            GLES20.glLinkProgram(program);
        }

        @Override
        public void onSurfaceChanged(
                javax.microedition.khronos.opengles.GL10 gl,
                int width,
                int height) {

            GLES20.glViewport(
                    0,
                    0,
                    width,
                    height
            );

            float ratio =
                    (float) width / (float) height;

            Matrix.frustumM(
                    projection,
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
        public void onDrawFrame(
                javax.microedition.khronos.opengles.GL10 gl) {

            GLES20.glClear(
                    GLES20.GL_COLOR_BUFFER_BIT |
                    GLES20.GL_DEPTH_BUFFER_BIT
            );

            Matrix.setLookAtM(
                    view,
                    0,
                    cameraX,
                    cameraY + 3,
                    10,
                    0,
                    0,
                    0,
                    0,
                    1,
                    0
            );

            drawGround();
            drawExcavator();

            drawCabin();
            drawArm();
            drawBucket();
        }

        private void drawGround() {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    0,
                    -2.2f,
                    0
            );

            Matrix.scaleM(
                    model,
                    0,
                    8,
                    0.2f,
                    8
            );

            drawCube(
                    model,
                    0.25f,
                    0.28f,
                    0.20f,
                    1
            );
        }

        private void drawExcavator() {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    0,
                    -1.0f,
                    0
            );

            Matrix.scaleM(
                    model,
                    0,
                    2.4f,
                    0.55f,
                    1.3f
            );

            drawCube(
                    model,
                    0.95f,
                    0.65f,
                    0.05f,
                    1
            );

            drawWheel(-1.5f, -1.55f);
            drawWheel(1.5f, -1.55f);
        }

        private void drawWheel(
                float x,
                float z) {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    x,
                    -1.55f,
                    z
            );

            Matrix.scaleM(
                    model,
                    0,
                    0.45f,
                    0.45f,
                    0.45f
            );

            drawCube(
                    model,
                    0.05f,
                    0.05f,
                    0.05f,
                    1
            );
        }

        private void drawCabin() {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    0.4f,
                    0.0f,
                    0
            );

            Matrix.scaleM(
                    model,
                    0,
                    0.9f,
                    1.0f,
                    0.9f
            );

            drawCube(
                    model,
                    0.10f,
                    0.30f,
                    0.65f,
                    1
            );
        }

        private void drawArm() {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    -1.4f,
                    0.4f,
                    0
            );

            Matrix.rotateM(
                    model,
                    0,
                    -25,
                    0,
                    0,
                    1
            );

            Matrix.scaleM(
                    model,
                    0,
                    1.8f,
                    0.25f,
                    0.25f
            );

            drawCube(
                    model,
                    0.90f,
                    0.55f,
                    0.05f,
                    1
            );
        }

        private void drawBucket() {

            Matrix.setIdentityM(model, 0);

            Matrix.translateM(
                    model,
                    0,
                    -2.7f,
                    -0.2f,
                    0
            );

            Matrix.rotateM(
                    model,
                    0,
                    -20,
                    0,
                    0,
                    1
            );

            Matrix.scaleM(
                    model,
                    0,
                    0.7f,
                    0.5f,
                    0.8f
            );

            drawCube(
                    model,
                    0.70f,
                    0.45f,
                    0.02f,
                    1
            );
        }

        private void drawCube(
                float[] matrix,
                float r,
                float g,
                float b,
                float a) {

            Matrix.multiplyMM(
                    mvp,
                    0,
                    view,
                    0,
                    matrix,
                    0
            );

            Matrix.multiplyMM(
                    mvp,
                    0,
                    projection,
                    0,
                    mvp,
                    0
            );

            GLES20.glUseProgram(program);

            int positionHandle =
                    GLES20.glGetAttribLocation(
                            program,
                            "vPosition"
                    );

            int matrixHandle =
                    GLES20.glGetUniformLocation(
                            program,
                            "uMVP"
                    );

            int colorHandle =
                    GLES20.glGetUniformLocation(
                            program,
                            "uColor"
                    );

            GLES20.glEnableVertexAttribArray(
                    positionHandle
            );

            GLES20.glVertexAttribPointer(
                    positionHandle,
                    3,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    cubeBuffer
            );

            GLES20.glUniformMatrix4fv(
                    matrixHandle,
                    1,
                    false,
                    mvp,
                    0
            );

            GLES20.glUniform4f(
                    colorHandle,
                    r,
                    g,
                    b,
                    a
            );

            GLES20.glDrawArrays(
                    GLES20.GL_TRIANGLE_FAN,
                    0,
                    8
            );

            GLES20.glDisableVertexAttribArray(
                    positionHandle
            );
        }

        private int loadShader(
                int type,
                String code) {

            int shader =
                    GLES20.glCreateShader(type);

            GLES20.glShaderSource(
                    shader,
                    code
            );

            GLES20.glCompileShader(shader);

            return shader;
        }
    }
}
