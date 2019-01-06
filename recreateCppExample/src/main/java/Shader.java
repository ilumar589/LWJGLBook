import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
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

}
