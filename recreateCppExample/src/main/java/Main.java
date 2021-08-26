import generic.Shader;
import org.lwjgl.system.Configuration;

import static examples.Examples.exCoordinateSystems;


public class Main {

    public static void main(String[] args) {

        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);

//        exCoordinateSystems();

        Shader shader = new Shader("vertex.glsl", "fragment.glsl");
    }
}
