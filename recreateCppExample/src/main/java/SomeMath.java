import org.joml.Matrix4f;
import org.joml.Vector3f;

public class SomeMath {

    public static void someMathFunc() {
        Vector3f vec = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f transformedVector = new Matrix4f()
                .translate(1.0f, 2.0f, 0.0f)
                .scale(2f)
                .transformPosition(vec);

        System.out.println(transformedVector.toString());
    }
}
