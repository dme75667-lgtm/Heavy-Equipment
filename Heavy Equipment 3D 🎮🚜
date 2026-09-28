package com.mehdi.heavyequipment;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Game3DView extends GLSurfaceView {

    final Renderer3D renderer;
    float downX, downY;

    public Game3DView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new Renderer3D();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = x;
            downY = y;
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            float dx = x - downX;
            float dy = y - downY;

            if (y < getHeight() * 0.55f) {
                renderer.rotateCamera(dx, dy);
            }

            downX = x;
            downY = y;

            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {

            float w = getWidth();
            float h = getHeight();

            if (y > h * 0.70f) {

                if (x < w * 0.18f) {
                    renderer.move(-1);
                }
                else if (x < w * 0.36f) {
                    renderer.move(1);
                }
                else if (x > w * 0.82f) {
                    renderer.action();
                }
            }

            return true;
        }

        return true;
    }
}


class Renderer3D implements GLSurfaceView.Renderer {

    float[] projection = new float[16];
    float[] view = new float[16];
    float[] vp = new float[16];

    float cameraYaw = 42;
    float cameraPitch = 24;
    float cameraDistance = 18;

    float machineX = 0;
    float machineZ = 0;

    int machine = 0;

    /*
       0 = حفارة
       1 = شارجور
       2 = شاحنة
    */

    int money = 25000;
    int fuel = 100;
    int load = 0;
    int delivered = 0;
    int stage = 1;

    SimpleShader shader = new SimpleShader();

    @Override
    public void onSurfaceCreated(
            javax.microedition.khronos.egl.EGLConfig config) {

        GLES20.glClearColor(
                0.56f,
                0.72f,
                0.84f,
                1
        );

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
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

        Matrix.perspectiveM(
                projection,
                0,
                52f,
                (float) width / height,
                0.1f,
                100f
        );
    }

    @Override
    public void onDrawFrame(
            javax.microedition.khronos.opengles.GL10 gl) {

        GLES20.glClear(
                GLES20.GL_COLOR_BUFFER_BIT |
                GLES20.GL_DEPTH_BUFFER_BIT
        );

        float yaw =
                (float)Math.toRadians(cameraYaw);

        float pitch =
                (float)Math.toRadians(cameraPitch);

        float camX =
                (float)Math.sin(yaw)
                * cameraDistance;

        float camZ =
                (float)Math.cos(yaw)
                * cameraDistance;

        float camY =
                (float)Math.sin(pitch)
                * cameraDistance + 5;

        Matrix.setLookAtM(
                view,
                0,

                camX + machineX,
                camY,
                camZ + machineZ,

                machineX,
                1,
                machineZ,

                0,
                1,
                0
        );

        Matrix.multiplyMM(
                vp,
                0,
                projection,
                0,
                view,
                0
        );

        drawTerrain();
        drawRoad();
        drawMountains();

        drawMachine(
                machineX,
                0,
                machineZ
        );

        drawTruck(
                6,
                0,
                -2
        );

        drawLoader(
                -6,
                0,
                3
        );

        drawTrees();
    }

    void rotateCamera(
            float dx,
            float dy) {

        cameraYaw += dx * 0.35f;

        cameraPitch -= dy * 0.18f;

        cameraPitch =
                Math.max(
                        10,
                        Math.min(
                                45,
                                cameraPitch
                        )
                );
    }

    void move(int direction) {

        float angle =
                (float)Math.toRadians(cameraYaw);

        machineX +=
                (float)Math.sin(angle)
                * direction
                * 0.45f;

        machineZ +=
                (float)Math.cos(angle)
                * direction
                * 0.45f;

        fuel =
                Math.max(
                        0,
                        fuel - 1
                );
    }

    void action() {

        if (machine == 0) {

            // الحفارة
            if (fuel > 0) {

                load =
                        Math.min(
                                5,
                                load + 1
                        );

                fuel--;
            }

        } else if (machine == 2) {

            // الشاحنة
            if (load > 0) {

                delivered++;

                money +=
                        6500 + stage * 750;

                load = 0;

                if (delivered >= 12) {

                    stage++;

                    delivered = 0;

                    money +=
                            30000;
                }
            }

        } else {

            // تغيير المعدة
            machine =
                    (machine + 1) % 3;
        }
    }

    void drawTerrain() {

        shader.begin(vp);

        shader.box(
                0,
                -0.55f,
                0,
                30,
                0.8f,
                24,
                0.40f,
                0.34f,
                0.22f
        );

        shader.end();
    }

    void drawRoad() {

        shader.begin(vp);

        shader.box(
                0,
                0.02f,
                0,
                4,
                0.08f,
                22,
                0.45f,
                0.38f,
                0.27f
        );

        shader.end();
    }

    void drawMountains() {

        shader.begin(vp);

        for (int i = -5; i <= 5; i++) {

            shader.pyramid(
                    i * 5,
                    1,
                    -11,
                    3.8f,
                    4.5f,
                    0.24f,
                    0.29f,
                    0.25f
            );
        }

        shader.end();
    }

    void drawMachine(
            float x,
            float y,
            float z) {

        shader.begin(vp);

        if (machine == 0) {

            drawExcavator(
                    x,
                    y,
                    z
            );

        } else if (machine == 1) {

            drawLoader(
                    x,
                    y,
                    z
            );

        } else {

            drawDumpTruck(
                    x,
                    y,
                    z
            );
        }

        shader.end();
    }

    void drawExcavator(
            float x,
            float y,
            float z) {

        // جسم الحفارة
        shader.box(
                x,
                y + 0.65f,
                z,
                2.8f,
                1.0f,
                1.9f,
                0.95f,
                0.60f,
                0.03f
        );

        // الكابينة
        shader.box(
                x,
                y + 1.15f,
                z,
                1.35f,
                0.9f,
                1.25f,
                0.08f,
                0.10f,
                0.08f
        );

        // الجنزير
        shader.box(
                x,
                y + 0.18f,
                z - 0.72f,
                3.3f,
                0.35f,
                0.45f,
                0.08f,
                0.07f,
                0.05f
        );

        shader.box(
                x,
                y + 0.18f,
                z + 0.72f,
                3.3f,
                0.35f,
                0.45f,
                0.08f,
                0.07f,
                0.05f
        );

        // ذراع الحفارة
        shader.box(
                x + 0.95f,
                y + 1.5f,
                z,
                2.8f,
                0.28f,
                0.28f,
                0.10f,
                0.08f,
                0.02f
        );

        shader.box(
                x + 2.25f,
                y + 1.75f,
                z,
                1.9f,
                0.25f,
                0.25f,
                0.10f,
                0.08f,
                0.02f
        );

        // الدلو
        shader.box(
                x + 3.1f,
                y + 1.45f,
                z,
                1.0f,
                0.35f,
                1.0f,
                0.25f,
                0.18f,
                0.10f
        );
    }

    void drawLoader(
            float x,
            float y,
            float z) {

        shader.box(
                x,
                y + 0.65f,
                z,
                2.8f,
                1.1f,
                1.8f,
                0.95f,
                0.60f,
                0.03f
        );

        shader.box(
                x + 0.2f,
                y + 1.35f,
                z,
                1.2f,
                1.0f,
                1.2f,
                0.08f,
                0.10f,
                0.08f
        );

        // ذراع الشارجور
        shader.box(
                x + 1.7f,
                y + 0.45f,
                z,
                2.2f,
                0.18f,
                0.25f,
                0.10f,
                0.08f,
                0.02f
        );

        // الدلو
        shader.box(
                x + 2.55f,
                y + 0.55f,
                z,
                1.3f,
                0.6f,
                1.8f,
                0.78f,
                0.55f,
                0.08f
        );

        shader.wheel(
                x - 1,
                y + 0.25f,
                z - 1,
                0.55f
        );

        shader.wheel(
                x - 1,
                y + 0.25f,
                z + 1,
                0.55f
        );

        shader.wheel(
                x + 1,
                y + 0.25f,
                z - 1,
                0.55f
        );

        shader.wheel(
                x + 1,
                y + 0.25f,
                z + 1,
                0.55f
        );
    }

    void drawTruck(
            float x,
            float y,
            float z) {

        shader.begin(vp);

        drawDumpTruck(
                x,
                y,
                z
        );

        shader.end();
    }

    void drawDumpTruck(
            float x,
            float y,
            float z) {

        shader.box(
                x,
                y + 0.7f,
                z,
                3.2f,
                1.2f,
                1.8f,
                0.12f,
                0.45f,
                0.75f
        );

        shader.box(
                x - 1,
                y + 1.3f,
                z,
                1.2f,
                1.3f,
                1.65f,
                0.82f,
                0.82f,
                0.82f
        );

        shader.box(
                x + 1,
                y + 1.55f,
                z,
                2.1f,
                1.0f,
                1.7f,
                0.35f,
                0.25f,
                0.12f
        );

        shader.wheel(
                x - 1,
                y + .18f,
                z - 1,
                .55f
        );

        shader.wheel(
                x - 1,
                y + .18f,
                z + 1,
                .55f
        );

        shader.wheel(
                x + 1,
                y + .18f,
                z - 1,
                .55f
        );

        shader.wheel(
                x + 1,
                y + .18f,
                z + 1,
                .55f
        );
    }

    void drawTrees() {

        shader.begin(vp);

        for (int i = 0; i < 8; i++) {

            float x =
                    -13 + i * 3.7f;

            float z =
                    -4 + (i % 4) * 3;

            shader.box(
                    x,
                    .8f,
                    z,
                    .25f,
                    1.6f,
                    .25f,
                    .25f,
                    .16f,
                    .08f
            );

            shader.pyramid(
                    x,
                    2,
                    z,
                    1.4f,
                    2.2f,
                    .12f,
                    .28f,
                    .10f
            );
        }

        shader.end();
    }
}


class SimpleShader {

    int program;
    int position;
    int color;
    int mvp;

    float[] model =
            new float[16];

    float[] finalMatrix =
            new float[16];

    float[] currentVP;

    FloatBuffer vertices;
    ShortBuffer indices;

    final float[] cube = {

            -.5f,-.5f,-.5f,
             .5f,-.5f,-.5f,
             .5f,.5f,-.5f,
            -.5f,.5f,-.5f,

            -.5f,-.5f,.5f,
             .5f,-.5f,.5f,
             .5f,.5f,.5f,
            -.5f,.5f,.5f
    };

    final short[] cubeIndex = {

            0,1,2,
            0,2,3,

            4,6,5,
            4,7,6,

            0,4,5,
            0,5,1,

            3,2,6,
            3,6,7,

            1,5,6,
            1,6,2,

            0,3,7,
            0,7,4
    };

    void begin(float[] vp) {

        currentVP = vp;

        if (program == 0)
            init();

        GLES20.glUseProgram(program);

        GLES20.glEnableVertexAttribArray(
                position
        );
    }

    void init() {

        String vertexShader =
                "attribute vec3 aPos;" +
                "uniform mat4 uMVP;" +
                "void main(){" +
                "gl_Position=uMVP*vec4(aPos,1.0);" +
                "}";

        String fragmentShader =
                "precision mediump float;" +
                "uniform vec4 uColor;" +
                "void main(){" +
                "gl_FragColor=uColor;" +
                "}";

        int v =
                GLES20.glCreateShader(
                        GLES20.GL_VERTEX_SHADER
                );

        GLES20.glShaderSource(
                v,
                vertexShader
        );

        GLES20.glCompileShader(v);

        int f =
                GLES20.glCreateShader(
                        GLES20.GL_FRAGMENT_SHADER
                );

        GLES20.glShaderSource(
                f,
                fragmentShader
        );

        GLES20.glCompileShader(f);

        program =
                GLES20.glCreateProgram();

        GLES20.glAttachShader(
                program,
                v
        );

        GLES20.glAttachShader(
                program,
                f
        );

        GLES20.glLinkProgram(program);

        position =
                GLES20.glGetAttribLocation(
                        program,
                        "aPos"
                );

        color =
                GLES20.glGetUniformLocation(
                        program,
                        "uColor"
                );

        mvp =
                GLES20.glGetUniformLocation(
                        program,
                        "uMVP"
                );

        ByteBuffer buffer =
                ByteBuffer.allocateDirect(
                        cube.length * 4
                ).order(
                        ByteOrder.nativeOrder()
                );

        vertices =
                buffer.asFloatBuffer();

        vertices.put(cube);
        vertices.position(0);

        ByteBuffer indexBuffer =
                ByteBuffer.allocateDirect(
                        cubeIndex.length * 2
                ).order(
                        ByteOrder.nativeOrder()
                );

        indices =
                indexBuffer.asShortBuffer();

        indices.put(cubeIndex);
        indices.position(0);
    }

    void box(
            float x,
            float y,
            float z,
            float sx,
            float sy,
            float sz,
            float r,
            float g,
            float b) {

        Matrix.setIdentityM(
                model,
                0
        );

        Matrix.translateM(
                model,
                0,
                x,
                y,
                z
        );

        Matrix.scaleM(
                model,
                0,
                sx,
                sy,
                sz
        );

        Matrix.multiplyMM(
                finalMatrix,
                0,
                currentVP,
                0,
                model,
                0
        );

        GLES20.glUniformMatrix4fv(
                mvp,
                1,
                false,
                finalMatrix,
                0
        );

        GLES20.glUniform4f(
                color,
                r,
                g,
                b,
                1
        );

        vertices.position(0);

        GLES20.glVertexAttribPointer(
                position,
                3,
                GLES20.GL_FLOAT,
                false,
                0,
                vertices
        );

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                36,
                GLES20.GL_UNSIGNED_SHORT,
                indices
        );
    }

    void wheel(
            float x,
            float y,
            float z,
            float radius) {

        box(
                x,
                y,
                z,
                .75f,
                radius * 1.2f,
                radius * 1.2f,
                .10f,
                .10f,
                .10f
        );
    }

    void pyramid(
            float x,
            float y,
            float z,
            float size,
            float height,
            float r,
            float g,
            float b) {

        box(
                x,
                y + height * .35f,
                z,
                size * .8f,
                height * .7f,
                size * .8f,
                r,
                g,
                b
        );

        box(
                x,
                y + height * .72f,
                z,
                size * .45f,
                height * .3f,
                size * .45f,
                r + .04f,
                g + .04f,
                b + .04f
        );
    }

    void end() {

        GLES20.glDisableVertexAttribArray(
                position
        );
    }
}
