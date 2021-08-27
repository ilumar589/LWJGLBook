package examples;

import generic.Shader;
import generic.Texture;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Examples {

    // examples code from learnopengl.com, 6.1 - coordinate systems
    public static void exCoordinateSystems() {
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

        // build and compile our shader program
        Shader shader = new Shader("/6.1.coordinate_systems_vs.glsl", "/6.1.coordinate_systems_fs.glsl");


        // set up vertex data

        float[] vertices = {
                // positions          // texture coords
                0.5f,  0.5f, 0.0f,   1.0f, 1.0f, // top right
                0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // bottom right
                -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // bottom left
                -0.5f,  0.5f, 0.0f,   0.0f, 1.0f  // top left
        };

        int[] indices = {
                0, 1, 3, // first triangle
                1, 2, 3  // second triangle
        };

        int VBO, VAO, EBO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // texture coord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // load image, create texture and generate mipmaps
        stbi_set_flip_vertically_on_load(true);
        Texture texture1 = new Texture(glGenTextures(), "wall.jpg");
        Texture texture2 = new Texture(glGenTextures(), "awesome-face.png");

        shader.use();
        shader.setInt("texture1", 0);
        shader.setInt("texture2", 1);

        while (!glfwWindowShouldClose(windowHandle)) {

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // bind texture on corresponding texture units
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture1.textureHandle());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture2.textureHandle());

            // activate shader
            shader.use();

            // create transformations
            Matrix4f model = new Matrix4f();
            Matrix4f view = new Matrix4f();
            Matrix4f projection = new Matrix4f();
            model = model.rotate(-55f, 1.0f, 0.0f, 0.0f);
            view = view.translate(0.0f, 0.0f, -3.0f);
            projection = projection.perspective(45.0f, 800f / 600f, 0.1f, 100.0f);
            shader.setMat4("model", model);
            shader.setMat4("view", view);
            // note: currently we set the projection matrix each frame, but since the projection matrix rarely changes it's often best practice to set it outside the main loop only once.
            shader.setMat4("projection", projection);

            // render container
            glBindVertexArray(VAO);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

            // check and call events and swap the buffers
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }

        glfwFreeCallbacks(windowHandle);
        glfwDestroyWindow(windowHandle);
        glfwMakeContextCurrent(NULL);
        GL.setCapabilities(null);
        glfwTerminate();
    }
}
