package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

public class Main {
    public static float[] pozice = {
            0.05f, 0.05f, 0.0f, // 0 -> Top right
            0.05f, -0.05f, 0.0f, // 1 -> Bottom right
            -0.05f, -0.05f, 0.0f, // 2 -> Bottom left
            -0.05f, 0.05f, 0.0f, // 3 -> Top left
    };
    public static float[] pozice2 = {
            0.15f, 0.15f, 0.0f, // 0 -> Top right
            0.15f, -0.15f, 0.0f, // 1 -> Bottom right
            -0.15f, -0.15f, 0.0f, // 2 -> Bottom left
            -0.15f, 0.15f, 0.0f, // 3 -> Top left
    };

    public static void main(String[] args) throws Exception {
        //region: Window init
        GLFW.glfwInit();
        // Tell GLFW what version of OpenGL we want to use.
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        // Create the window...
        // We can set multiple options with glfwWindowHint ie. fullscreen, resizability etc.
        long window = GLFW.glfwCreateWindow(800, 600, "My first window", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("Can't open window");
        }
        GLFW.glfwMakeContextCurrent(window);
        // Tell GLFW, that we are using OpenGL
        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);
        // Resize callback
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> GL33.glViewport(0, 0, w, h));
        Shaders.initShaders();


        Square sq1 = new Square();
        sq1.setVrcholy(pozice);
        Square sq2 = new Square();
        sq2.setVrcholy(pozice2);

        // Draw in polygon mod
        //GL33.glPolygonMode(GL33.GL_FRONT_AND_BACK, GL33.GL_LINE);
        while (!GLFW.glfwWindowShouldClose(window)) {
            GL33.glUseProgram(Shaders.shaderProgramId);
            // Key input management
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true); // Send a shutdown signal...

            // Change the background color
            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);


            GL33.glBindVertexArray(sq1.getSquareVaoId());
            GL33.glDrawElements(GL33.GL_TRIANGLES, sq1.getIndexy(), GL33.GL_UNSIGNED_INT, 0);

            GL33.glBindVertexArray(sq2.getSquareVaoId());
            GL33.glDrawElements(GL33.GL_TRIANGLES, sq2.getIndexy(), GL33.GL_UNSIGNED_INT, 0);

            GLFW.glfwSwapBuffers(window);// Swap the color buffer -> screen tearing solution
            GLFW.glfwPollEvents();// Listen to input
        }

        // Don't forget to cleanup
        GLFW.glfwTerminate();
    }

}
