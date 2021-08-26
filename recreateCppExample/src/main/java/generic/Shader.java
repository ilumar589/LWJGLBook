package generic;

import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public final class Shader {

    private final int shaderProgramId;

    public Shader(String vertexPath, String fragmentPath) {
        final String vertexCode = Utils.loadResource(vertexPath);
        final String fragmentCode = Utils.loadResource(fragmentPath);

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
    }

    public void use() {
        glUseProgram(shaderProgramId);
    }

    public void setBool(String name, boolean value) {
        setInt(name, value ? 1 : 0);
    }

    public void setInt(String name, int value) {
        glUniform1i(glGetUniformLocation(shaderProgramId, name), value);
    }

    public void setFloat(String name, float value) {
        glUniform1f(glGetUniformLocation(shaderProgramId, name), value);
    }

    public void setVec2(String name, Vector2f value) {
        final float[] v = {
                value.x(),
                value.y()
        };

        glUniform2fv(glGetUniformLocation(shaderProgramId, name), v);
    }

    public void setVec2(String name, float x, float y) {
        glUniform2f(glGetUniformLocation(shaderProgramId, name), x, y);
    }

    public void setVec3(String name, Vector3f value) {
        final float[] v = {
                value.x(),
                value.y(),
                value.z()
        };

        glUniform3fv(glGetUniformLocation(shaderProgramId, name), v);
    }

    public void setVec3(String name, float x, float y, float z) {
        glUniform3f(glGetUniformLocation(shaderProgramId, name), x, y, z);
    }

    public void setVec4(String name, Vector4f value) {
        final float[] v = {
                value.x(),
                value.y(),
                value.z(),
                value.w()
        };

        glUniform4fv(glGetUniformLocation(shaderProgramId, name), v);
    }

    public void setVec4(String name, float x, float y, float z, float w) {
        glUniform4f(glGetUniformLocation(shaderProgramId, name), x, y, z, w);
    }

    public void setMat2(String name, Matrix2f value) {
        float[] mArray = new float[4];
        glUniformMatrix2fv(glGetUniformLocation(shaderProgramId, name), false, value.get(mArray));
    }

    public void setMat3(String name, Matrix3f value) {
        float[] mArray = new float[6];
        glUniformMatrix3fv(glGetUniformLocation(shaderProgramId, name), false, value.get(mArray));
    }
    public void setMat4(String name, Matrix4f value) {
        float[] mArray = new float[16];
        glUniformMatrix4fv(glGetUniformLocation(shaderProgramId, name), false, value.get(mArray));
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
