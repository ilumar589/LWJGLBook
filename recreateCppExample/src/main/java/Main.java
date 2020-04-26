import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.system.MemoryUtil.*;


public class Main {

    public static void main(String[] args) {

        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);

        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        long windowHandle = glfwCreateWindow(800, 600, "LearnOpenGl", NULL, NULL);

        if (windowHandle == NULL) {
            System.out.println("Failed to create GLFW window");
            glfwMakeContextCurrent(NULL);
            GL.setCapabilities(null);
            glfwTerminate();
        }

        glfwMakeContextCurrent(windowHandle);

        // Make the window visible
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glViewport(0, 0, 800, 600);

        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> glViewport(0, 0, width, height));

        Shader shader = new Shader("vertex.glsl", "fragment.glsl");
        Texture texture = new Texture(glGenTextures(), "wall.jpg");

        // setup triangle vertices
        float[] firstTriangleData = {
                // positions            // colors           // texture coordinates
                -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f
        };

        float[] secondTriangleData = {
                // positions            // colors           // texture coordinates
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.5f, 1.0f
        };

        TriangleGpuData firstTriangle = new TriangleGpuData(firstTriangleData);
        TriangleGpuData secondTriangle = new TriangleGpuData(secondTriangleData);

        GpuData.unbindData();

        while (!glfwWindowShouldClose(windowHandle)) {

            texture.useTexture();
            shader.use();

            glBindVertexArray(firstTriangle.getVertexArrayObject());
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glBindVertexArray(secondTriangle.getVertexArrayObject());
            glDrawArrays(GL_TRIANGLES, 0, 3);

            // check and call events and swap the buffers
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
        }

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwMakeContextCurrent(NULL);
        GL.setCapabilities(null);
        glfwTerminate();
    }
}
