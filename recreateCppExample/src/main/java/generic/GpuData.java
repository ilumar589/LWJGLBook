package generic;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public abstract class GpuData {
    protected int vertexArrayObject;

    public int getVertexArrayObject() {
        return vertexArrayObject;
    }

    public static void unbindData() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
