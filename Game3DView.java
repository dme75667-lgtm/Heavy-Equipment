package com.mehdi.heavyequipment;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

        private FloatBuffer vertices;
        private int program;

        private final float[] cube = {
                -1, -1, 1,
                 1, -1, 1,
                 1,  1, 1,
                -1,  1, 1,

                -1, -1, -1,
                -1,  1, -1,
                 1,  1, -1,
                 1, -1, -1
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

            vertices = ByteBuffer
                    .allocateDirect(cube.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();

            vertices.put(cube);
            vertices.position(0);

            String vertexShader =
                    "attribute vec4 vPosition;" +
                    "uniform mat4 uMVP;" +
                    "void main() {" +
                    "    gl_Position = uMVP * vPosition;" +
                    "}";

            String fragmentShader =
                    "precision mediump float;" +
                    "void main() {" +
                    "    gl_FragColor = vec4(0.18, 0.22, 0.12, 1.0);" +
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

            float ratio = (float) width / height;

            Matrix.frustumM(
                    projection,
                    0,
                    -ratio,
                    ratio,
                    -1,
                    1,
                    3,
                    20
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
                    0,
                    3,
                    7,
                    0,
                    0,
                    0,
                    0,
                    1,
                    0
            );

            Matrix.setIdentityM(model, 0);

            Matrix.multiplyMM(
                    mvp,
                    0,
                    view,
                    0,
                    model,
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

            GLES20.glEnableVertexAttribArray(
                    positionHandle
            );

            GLES20.glVertexAttribPointer(
                    positionHandle,
                    3,
                    GLES20.GL_FLOAT,
                    false,
                    0,
                    vertices
            );

            GLES20.glUniformMatrix4fv(
                    matrixHandle,
                    1,
                    false,
                    mvp,
                    0
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
