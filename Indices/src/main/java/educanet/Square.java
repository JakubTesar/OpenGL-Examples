package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {
    private float[] vertices;

    private final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    public int squareVaoId;
    public int squareVboId;
    private int squareEboId;
    private int squareColorId;

    public static int uniformMatrixLocation;
    public Matrix4f matrix;
    public FloatBuffer matrixFloatBuffer;

    public float[] redCl;
    public float[] whiteCl;

    private float x;
    private float y;
    private float s;

    private FloatBuffer cfb;


    public Square(float x, float y, float s) {
        this.x = x;
        this.y = y;
        this.s = s;
        matrix = new Matrix4f().identity();
        matrixFloatBuffer = BufferUtils.createFloatBuffer(16);

        float[] colors = {
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
        };

        whiteCl = new float[]{
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
        };

        float[] vertices = {
                x + s, y, 0.0f, // 0 -> Top right
                x + s, y - s, 0.0f, // 1 -> Bottom right
                x, y - s, 0.0f, // 2 -> Bottom left
                x, y, 0.0f, // 3 -> Top left
        };

        this.vertices = vertices;

        squareVaoId = GL33.glGenVertexArrays();
        squareEboId = GL33.glGenBuffers();
        squareVboId = GL33.glGenBuffers();
        squareColorId = GL33.glGenBuffers();

        uniformMatrixLocation = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        GL33.glBindVertexArray(squareVaoId);

        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        FloatBuffer cfb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        MemoryUtil.memFree(cfb);

        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);
        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);

        GL33.glUseProgram(Shaders.shaderProgramId);
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(ib);
    }

    public void render() {
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);

        GL33.glUseProgram(Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    private float xm = 0.01f;
    private float ym = 0.01f;

    public void update(long window) {
        matrix = matrix.translate(xm, ym, 0f);
        x += xm;
        y += ym;

        if (x + s > 1 || x < -1) {
            xm *= -1;
        }
        if (y > 1 || y - s < -1) {
            ym *= -1;
        }
        matrix.get(matrixFloatBuffer);
        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
    }

    public void white() {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareColorId);
        cfb.put(whiteCl).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getS() {
        return s;
    }
}