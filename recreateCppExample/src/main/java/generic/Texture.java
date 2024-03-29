package generic;

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

public record Texture(int textureHandle, String textureLocation) {

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
        useTexture();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        ByteBuffer image;
        try {
            image = Utils.ioResourceToByteBuffer(textureLocation, 8 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // Use info to read image metadata without decoding the entire image.
            // We don't need this for this demo, just testing the API.
            if (!stbi_info_from_memory(image, w, h, comp)) {
                throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());
            }

            // limit size
//            w.put(512);
//            w.flip();
//            h.put(512);
//            h.flip();

            System.out.println("Image width: " + w.get(0));
            System.out.println("Image height: " + h.get(0));
            System.out.println("Image components: " + comp.get(0));
            System.out.println("Image HDR: " + stbi_is_hdr_from_memory(image));

            ByteBuffer decodedImage = stbi_load_from_memory(image, w, h, comp, 0);
            if (decodedImage == null) {
                throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
            }

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w.get(), h.get(), 0, GL_RGB, GL_UNSIGNED_BYTE, decodedImage);
            glGenerateMipmap(GL_TEXTURE_2D);

            // by comparing content the image buffers don't seem to be pointing at the same data in memory
            // hopefully we don't get undefined behaviour
            stbi_image_free(decodedImage);
            memFree(image);
        }
    }
}
