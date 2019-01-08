import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Shader {

    public int shaderProgramId;

    public Shader(String vertexPath, String fragmentPath) {
        try {
            String vertexCode =  Utils.loadResource(vertexPath);
            String fragmentCode = Utils.loadResource(fragmentPath);

            //====== VERTEX SHADER ======
            int vertexShaderId = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertexShaderId, vertexCode);
            glCompileShader(vertexShaderId);
            logShaderStatus(vertexShaderId, GL_COMPILE_STATUS);

            //====== FRAGMENT SHADER =====
            int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentShaderId, fragmentCode);
            glCompileShader(fragmentShaderId);
            logShaderStatus(fragmentShaderId, GL_COMPILE_STATUS);

            //===== LINK SHADERS TO A SHADER PROGRAM AND RELEASE SHADER RESOURCES
            shaderProgramId = glCreateProgram();
            glAttachShader(shaderProgramId, vertexShaderId);
            glAttachShader(shaderProgramId, fragmentShaderId);
            glLinkProgram(shaderProgramId);
            logShaderStatus(shaderProgramId, GL_LINK_STATUS);

            //=== DELETE IN MEMORY SHADERS BECAUSE THEY ARE ALREADY ATTACHED TO THE SHADER PROGRAM
            glDeleteShader(vertexShaderId);
            glDeleteShader(fragmentShaderId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void use() {
        glUseProgram(shaderProgramId);
    }

    private void logShaderStatus(int shaderId, int statusToCheck) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer success = stack.mallocInt(1);

            glGetShaderiv(shaderId, statusToCheck, success);

            if (success.get() == NULL) {
                System.out.println(glGetShaderInfoLog(shaderId));
            }
        }
    }

    // Util just for tests
    static int loadAndCreateTexture() {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        ByteBuffer imageBuffer;

        try {
            imageBuffer = Utils.ioResourceToByteBuffer("wall.jpg", 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        try (MemoryStack stack  = MemoryStack.stackPush()) {
            IntBuffer w    = stack.mallocInt(1);
            IntBuffer h    = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Use info to read image metadata without decoding the entire image.
            // We don't need this for this demo, just testing the API.
            if (!stbi_info_from_memory(imageBuffer, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            } else {
                System.out.println("OK with reason: " + stbi_failure_reason());
            }

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + comp.get(0));
            System.out.println("Image HDR: " + stbi_is_hdr_from_memory(imageBuffer));

            // Decode the image
            ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(), h.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(image);
        }

        return texture;
    }

}
