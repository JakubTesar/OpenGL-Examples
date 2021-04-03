package cz.educanet;

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
    public Square(float x, float y, float size) {
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
        GL33.glBindVertexArray(squareVaoId);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);
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
    public void render () {
        GL33.glUseProgram(cz.educanet.Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }
}