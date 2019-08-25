package org.lwjgl.engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private final int vaoId;
    private final int positionVboId;
    private final int indexVboId;
    private final int colorVboId;
    private final int vertexCount;

    public Mesh(float[] positions, int[] indices, float[] colors) {
        FloatBuffer positionBuffer = null;
        IntBuffer   indicesBuffer  = null;
        FloatBuffer colorBuffer    = null;
        try {
            vertexCount = indices.length;

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            positionVboId = glGenBuffers();
            positionBuffer = MemoryUtil.memAllocFloat(positions.length);
            positionBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, positionVboId);
            glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
            // our shader will be expecting the position data at index/layout 0
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Index VBO
            indexVboId = glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // Color VBO
            colorVboId = glGenBuffers();
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            // our shader will be expecting the color data at index/layout 1
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);


            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (positionBuffer != null) {
                MemoryUtil.memFree(positionBuffer);
            }

            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }

            if (colorBuffer != null) {
                MemoryUtil.memFree(colorBuffer);
            }
        }
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return  vertexCount;
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionVboId);
        glDeleteBuffers(indexVboId);
        glDeleteBuffers(colorVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
