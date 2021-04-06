package educanet;

import org.lwjgl.BufferUtils;
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
    private int squareVaoId;
    private int squareVboId;
    private int squareEboId;
    private int squareColorId;

    public Square(float x, float y, float size) {
        float longest = (float) Math.sqrt(3);

        float a1 = (float) Math.sqrt(((x + size) * (x + size)) + (y * y));  // 0 -> Top right
        float b1 = (float) Math.sqrt(((x + size) * (x + size)) + ((y - size) * (y - size)));// 1 -> Bottom right
        float c1 = (float) Math.sqrt((x * x) + ((y - size) * (y - size)));   // 2 -> Bottom left
        float d1 = (float) Math.sqrt((x * x) + (y * y));                     // 3 -> Top left

        float a2 = (a1 / longest);
        float b2 = (b1 / longest);
        float c2 = (c1 / longest);
        float d2 = (d1 / longest);

        float[] colors = {
                (float) (a2 + 0.5), (float) (a2 + 0.2), a2, 1f,
                (float) (b2 + 0.5), (float) (b2 + 0.2), b2, 1f,
                (float) (c2 + 0.5), (float) (c2 + 0.2), c2, 1f,
                (float) (d2 + 0.5), (float) (d2 + 0.2), d2, 1f,
        };
        float[] vertices = {
                x + size, y, 0.0f, // 0 -> Top right
                x + size, y - size, 0.0f, // 1 -> Bottom right
                x, y - size, 0.0f, // 2 -> Bottom left
                x, y, 0.0f, // 3 -> Top left
        };

        this.vertices = vertices;
        squareVaoId = GL33.glGenVertexArrays();
        squareEboId = GL33.glGenBuffers();
        squareVboId = GL33.glGenBuffers();
        squareColorId = GL33.glGenBuffers();

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
        MemoryUtil.memFree(fb);
        MemoryUtil.memFree(ib);
    }

    public void render() {
        GL33.glUseProgram(educanet.Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }
}