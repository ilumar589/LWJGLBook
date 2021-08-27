import org.lwjgl.system.Configuration;

import static examples.CoordinateSystemExamples.exCoordinateSystems;
import static examples.CoordinateSystemExamples.rotatingCube;


public class Main {

    public static void main(String[] args) {

        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);

//        exCoordinateSystems();

        rotatingCube();
    }
}
