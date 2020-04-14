import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Texture {

    private final int textureHandle;
    private final String textureLocation;

    // Util just for tests
    public Texture(int textureHandle, String textureLocation) {
        this.textureHandle = textureHandle;
        this.textureLocation = textureLocation;

        bindTexture();
    }

    public void useTexture() {
        // bind texture
        glBindTexture(GL_TEXTURE_2D, textureHandle);
    }

    private void bindTexture() {
        glBindTexture(GL_TEXTURE_2D, textureHandle);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        ByteBuffer imageBuffer;

        try {
            imageBuffer = Utils.ioResourceToByteBuffer(textureLocation, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException();
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
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
//            ByteBuffer image = stbi_load("D:\\WorkZone\\Projects\\JavaProjects\\LWJGLBook\\recreateCppExample\\src\\main\\resources\\wall.jpg", w, h, comp, 0);

            ByteBuffer image = stbi_load_from_memory(imageBuffer, w, h, comp, 0);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(), h.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(image);
            memFree(imageBuffer);
        }
    }
}
