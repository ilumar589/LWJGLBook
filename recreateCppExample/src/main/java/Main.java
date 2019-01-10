import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;


public class Main {

    public static void main(String[] args) {

        SomeMath.someMathFunc();

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        long windowHandle = glfwCreateWindow(800, 600, "LearnOpenGl", NULL, NULL);

        if (windowHandle == NULL) {
            System.out.println("Failed to create GLFW window");
            glfwTerminate();
        }

        glfwMakeContextCurrent(windowHandle);

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glViewport(0, 0, 800, 600);

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            glViewport(0, 0, width, height);
        });

        Shader shader = new Shader("vertex.glsl", "fragment.glsl");

        // setup triangle vertices
        float[] triangleData = {
                // positions            // colors           // texture coordinates
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f
        };

        int VAO, VBO;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer triangleDataBuffer = stack.mallocFloat(triangleData.length);
            triangleDataBuffer.put(triangleData).flip();

            VAO = glGenVertexArrays();
            VBO = glGenBuffers();

            glBindVertexArray(VAO);

            glBindBuffer(GL_ARRAY_BUFFER, VBO);

            glBufferData(GL_ARRAY_BUFFER, triangleDataBuffer, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);

            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
            glEnableVertexAttribArray(2);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        int texture = Shader.loadAndCreateTexture();

        while (!glfwWindowShouldClose(windowHandle)) {

            // bind texture
            glBindTexture(GL_TEXTURE_2D, texture);

            shader.use();

            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 3);


            // check and call events and swap the buffers
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

        }

        glfwTerminate();
    }
}
