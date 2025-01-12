package educanet;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {
    private float[] vertices;
    private float frame = 6;

    private final int[] indices = {
            0, 1, 3, // First triangle
            1, 2, 3 // Second triangle
    };

    public int squareVaoId;
    public int squareVboId;
    private int squareEboId;
    private int squareColorId;

    private static int textureIndicesId;
    private static int textureId;

    public static int uniformMatrixLocation;
    public Matrix4f matrix;
    public FloatBuffer matrixFloatBuffer;

    public float[] whiteCl;

    private float x;
    private float y;
    private float s;

    private FloatBuffer cfb;
    private FloatBuffer tb1 = BufferUtils.createFloatBuffer(8);


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

        textureIndicesId = GL33.glGenBuffers();
        textureId = GL33.glGenTextures();

        loadImage();


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

        // Clear the buffer from the memory (it's saved now on the GPU, no need for it here)
        MemoryUtil.memFree(cfb);
        MemoryUtil.memFree(tb1);

    }

    public void render() {
        GL33.glUseProgram(Shaders.shaderProgramId);

        // Draw using the glDrawElements function
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }

    public void update(long window) {
        float[] textures1 = {
                0.16f, 0.0f,
                0.16f, 1f,
                0.0f, 1f,
                0.0f, 0.0f,
        };
        float[] textures2 = {
                0.32f, 0.0f,
                0.32f, 1f,
                0.16f, 1f,
                0.16f, 0.0f,
        };
        float[] textures3 = {
                0.48f, 0.0f,
                0.48f, 1f,
                0.32f, 1f,
                0.32f, 0.0f,
        };

        float[] textures4 = {
                0.64f, 0.0f,
                0.64f, 1f,
                0.48f, 1f,
                0.48f, 0.0f,
        };

        float[] textures5 = {
                0.80f, 0.0f,
                0.80f, 1f,
                0.64f, 1f,
                0.64f, 0.0f,
        };
        float[] textures6 = {
                0.96f, 0.0f,
                0.96f, 1f,
                0.8f, 1f,
                0.8f, 0f,
        };


        if ((int)frame == 6) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);

            tb1.put(textures1).flip();

            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh1");
            frame -= 0.01;
        }
        if ((int)frame == 5) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);
            tb1.clear().put(textures2).flip();


            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh2");
            frame -= 0.01;
        }
        if ((int)frame == 4) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);
            tb1.clear()
                    .put(textures3)
                    .flip();

            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh3");
            frame -= 0.01;
        }
        if ((int)frame == 3) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);
            tb1.clear()
                    .put(textures4)
                    .flip();

            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh4");
            frame -= 0.01;

        }
        if ((int)frame == 2) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);
            tb1.clear()
                    .put(textures5)
                    .flip();

            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh5");
            frame -= 0.01;
        }
        if ((int)frame == 1) {
            GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, textureIndicesId);
            tb1.clear()
                    .put(textures6)
                    .flip();

            // Send the buffer (positions) to the GPU
            GL33.glBufferData(GL33.GL_ARRAY_BUFFER, tb1, GL33.GL_STATIC_DRAW);
            GL33.glVertexAttribPointer(2, 2, GL33.GL_FLOAT, false, 0, 0);
            GL33.glEnableVertexAttribArray(2);
            System.out.println("btuh6");
            frame = 6;
        }


        GL33.glUniformMatrix4fv(uniformMatrixLocation, false, matrixFloatBuffer);
    }

    private static void loadImage() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer img = STBImage.stbi_load("Cyborg_run.png", w, h, comp, 3);
            if (img != null) {
                img.flip();

                GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
                GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGB, w.get(), h.get(), 0, GL33.GL_RGB, GL33.GL_UNSIGNED_BYTE, img);
                GL33.glGenerateMipmap(GL33.GL_TEXTURE_2D);

                STBImage.stbi_image_free(img);
            }
        }
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