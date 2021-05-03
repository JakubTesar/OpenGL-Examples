package educanet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws Exception {
        String maze = "-0.19999999;-1.0;0.4\n" +
                "0.20000005;-1.0;0.4\n" +
                "0.6;-1.0;0.4\n" +
                "0.6;-0.6;0.4\n" +
                "-1.0;-0.19999999;0.4\n" +
                "-0.6;-0.19999999;0.4\n" +
                "-0.19999999;-0.19999999;0.4\n" +
                "0.6;-0.19999999;0.4\n" +
                "-1.0;0.20000005;0.4\n" +
                "-1.0;0.6;0.4\n" +
                "-0.6;0.6;0.4\n" +
                "-0.19999999;0.6;0.4\n";

        String[] xys = maze.split("\n");

        GLFW.glfwInit();
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
        long window = GLFW.glfwCreateWindow(800, 600, "Maze", 0, 0);
        if (window == 0) {
            GLFW.glfwTerminate();
            throw new Exception("Can't open window");
        }
        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL33.glViewport(0, 0, 800, 600);
        GLFW.glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            GL33.glViewport(0, 0, w, h);
        });
        Shaders.initShaders();

        ArrayList<Square> squares = new ArrayList<>();
        for (int i = 0; i < xys.length; i++) {
            String[] xys2 = xys[i].split(";");
            Square square = new Square(
                    Float.parseFloat(xys2[0]),
                    Float.parseFloat(xys2[1]),
                    Float.parseFloat(xys2[2]));
            squares.add(square);
        }

        Square moveSquare = new Square(0f, 0f, 0.25f);
        while (!GLFW.glfwWindowShouldClose(window)) {

            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true);
            GL33.glClearColor(0f, 0f, 0f, 1f);
            GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

            for (int i = 0; i < squares.size(); i++) {
                squares.get(i).render();
            }
            boolean koliduje = false;
            for (int i = 0; i < squares.size(); i++) {
                if (isInSquare(moveSquare, squares.get(i))) {
                    koliduje = true;
                    System.out.println(moveSquare.getY() + moveSquare.getS());
                    System.out.println(squares.get(i).getY());
                }
            }

            moveSquare.update(window);
            moveSquare.render();
            if (koliduje) {
                moveSquare.red();
            } else {
                moveSquare.white();
            }

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
        GLFW.glfwTerminate();
    }

    public static boolean isInSquare(Square m, Square s) {

        return (m.getX() + m.getS() > s.getX() && m.getX() < s.getX() + s.getS()
                && m.getY() + m.getS() / 2 + m.getS() > s.getY() && m.getY() + m.getS() / 2 < s.getY() + s.getS());
    }
}
