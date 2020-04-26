import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

final public class TriangleGpuData extends GpuData {

    public TriangleGpuData(float[] data) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer dataBuffer = stack.mallocFloat(data.length);
            dataBuffer.put(data);

            vertexArrayObject = glGenVertexArrays();
            int vertexBufferObject = glGenBuffers();

            glBindVertexArray(vertexArrayObject); // vertex attribute arrays are objects that allow us to define multiple vertex attribute pointers towards a vertex buffer object
            glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObject);
            glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
            glEnableVertexAttribArray(0);

            glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(1);

            glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
            glEnableVertexAttribArray(2);
        }
    }

}
