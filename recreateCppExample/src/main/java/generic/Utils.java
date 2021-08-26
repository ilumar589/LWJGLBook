package generic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Utils {

    public static Path getFilePathFromResource(String fileName) {
        URL resource = Utils.class.getResource(fileName);
        Path path = null;
        try {
            path = Paths.get(resource.toURI()).toAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return path;
    }

    public static String loadResource(String fileName) {
        String result = null;
        try (InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, String.valueOf(StandardCharsets.UTF_8))) {
            result = scanner.useDelimiter("\\A").next();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Warning, this version uses manual memory allocation/free for the byte buffer
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        try (
                InputStream source = Utils.class.getClassLoader().getResourceAsStream(resource);
                ReadableByteChannel rbc = Channels.newChannel(source)
        ) {
            buffer = memAlloc(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer.flip(), buffer.capacity() * 3 / 2); // 50%
                }
            }
        }

        buffer.flip();
        return buffer.slice();
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = memAlloc(newCapacity);
        newBuffer.put(buffer);

        memFree(buffer);

        return newBuffer;
    }
}