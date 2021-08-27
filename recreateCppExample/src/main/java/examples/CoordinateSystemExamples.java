package examples;

import generic.Shader;
import generic.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
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

public final class CoordinateSystemExamples {

    public static void multipleRotatingCubes() {
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

        // configure global opengl state
        // -----------------------------
        glEnable(GL_DEPTH_TEST);

        // build and compile our shader program
        Shader shader = new Shader("/6.3.coordinate_systems_vs.glsl", "/6.3.coordinate_systems_fs.glsl");

        // set up vertex data (and buffer(s)) and configure vertex attributes
        // ------------------------------------------------------------------
        float[] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };

        // world space positions of our cubes
        Vector3f[] cubePositions = {
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f( 2.0f,  5.0f, -15.0f),
                new Vector3f(-1.5f, -2.2f, -2.5f),
                new Vector3f(-3.8f, -2.0f, -12.3f),
                new Vector3f( 2.4f, -0.4f, -3.5f),
                new Vector3f(-1.7f,  3.0f, -7.5f),
                new Vector3f( 1.3f, -2.0f, -2.5f),
                new Vector3f( 1.5f,  2.0f, -2.5f),
                new Vector3f( 1.5f,  0.2f, -1.5f),
                new Vector3f(-1.3f,  1.0f, -1.5f)
        };

        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        //position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // texture coord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        Texture texture1 = new Texture(glGenTextures(), "wall.jpg");
        Texture texture2 = new Texture(glGenTextures(), "awesome-face.jpg");

        shader.use();
        shader.setInt("texture1", 0);
        shader.setInt("texture2", 1);

        while (!glfwWindowShouldClose(windowHandle)) {

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // bind texture on corresponding texture units
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture1.textureHandle());
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, texture2.textureHandle());

            // activate shader
            shader.use();

            // create transformations
            Matrix4f view = new Matrix4f();
            Matrix4f projection = new Matrix4f();
            view = view.translate(0.0f, 0.0f, -3.0f);
            projection = projection.perspective(90.0f, 800.0f / 600.0f, 0.1f, 100.0f);
            shader.setMat4("view", view);
            // note: currently we set the projection matrix each frame, but since the projection matrix rarely changes it's often best practice to set it outside the main loop only once.
            shader.setMat4("projection", projection);

            final Vector3f rotationVector = new Vector3f(1.0f, 0.3f, 0.5f);

            Matrix4f rotatingModel = new Matrix4f();
            rotatingModel = rotatingModel.translate(cubePositions[0]);
            rotatingModel = rotatingModel.rotate((float) glfwGetTime(), rotationVector);
            shader.setMat4("model", rotatingModel);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            rotatingModel = new Matrix4f()
                    .translate(cubePositions[3])
                    .rotate((float) glfwGetTime(), rotationVector);

            shader.setMat4("model", rotatingModel);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            rotatingModel = new Matrix4f()
                    .translate(cubePositions[7])
                    .rotate((float) glfwGetTime(), rotationVector);

            shader.setMat4("model", rotatingModel);
            glDrawArrays(GL_TRIANGLES, 0, 36);

            // render container
            glBindVertexArray(VAO);
            for (int i = 2; i < 10; i+=3) {

                // calculate the model matrix for each object and pass it to the shader before drawing
                Matrix4f model = new Matrix4f();
                model = model.translate(cubePositions[i]);
                float angle = 20.0f * i;
                model = model.rotate(angle, 1.0f, 0.3f, 0.5f);
                shader.setMat4("model", model);
                glDrawArrays(GL_TRIANGLES, 0, 36);
            }

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

    public static void rotatingCube() {
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

        // configure global opengl state
        // -----------------------------
        glEnable(GL_DEPTH_TEST);

        // build and compile our shader program
        Shader shader = new Shader("/6.2.coordinate_systems_vs.glsl", "/6.2.coordinate_systems_fs.glsl");

        // set up vertex data (and buffer(s)) and configure vertex attributes
        // ------------------------------------------------------------------
        float[] vertices = {
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
                0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };


        int VBO, VAO;
        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        //position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        // texture coord attribute
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        Texture texture1 = new Texture(glGenTextures(), "wall.jpg");
        Texture texture2 = new Texture(glGenTextures(), "awesome-face.jpg");

        shader.use();
        shader.setInt("texture1", 0);
        shader.setInt("texture2", 1);

        while (!glfwWindowShouldClose(windowHandle)) {

            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

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
            model = model.rotate((float) glfwGetTime(), 0.5f, 1.0f, 0.0f);
            view = view.translate(0.0f, 0.0f, -3.0f);
            projection = projection.perspective(90.0f, 800.0f / 600.0f, 0.1f, 100.0f);
            shader.setMat4("model", model);
            shader.setMat4("view", view);
            // note: currently we set the projection matrix each frame, but since the projection matrix rarely changes it's often best practice to set it outside the main loop only once.
            shader.setMat4("projection", projection);

            // render container
            glBindVertexArray(VAO);
            glDrawArrays(GL_TRIANGLES, 0, 36);

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
//        stbi_set_flip_vertically_on_load(true);
        Texture texture1 = new Texture(glGenTextures(), "wall.jpg");
        Texture texture2 = new Texture(glGenTextures(), "awesome-face.jpg");

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
            model = model.rotate(-35f, 1.0f, 0.0f, 0.0f);
            view = view.translate(0.0f, 0.0f, -3.0f);
            projection = projection.perspective(45.0f, 800.0f / 600.0f, 0.1f, 100.0f);
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
