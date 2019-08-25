import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkExtensionProperties;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.vulkan.VK10.*;

public class HelloTriangleApplication {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private long window;
    private VkInstance vkInstance;

    public void run() {
        initWindow();
        initVulkan();
        mainLoop();
        cleanUp();
    }

    private void initWindow() {
        glfwInit();
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        window = glfwCreateWindow(WIDTH, HEIGHT, "Vulkan", 0, 0);
    }

    private void initVulkan() {
        createInstance();
    }

    private void mainLoop() {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
        }
    }

    private void cleanUp() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void createInstance() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Charset defaultCharset = Charset.defaultCharset();

            VkApplicationInfo vkApplicationInfo = VkApplicationInfo.mallocStack(stack);

            vkApplicationInfo.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO);
            vkApplicationInfo.pApplicationName(Utils.strToBB("Hello Triangle", defaultCharset));
            vkApplicationInfo.applicationVersion(VK_MAKE_VERSION(1, 0, 0));
            vkApplicationInfo.pEngineName(Utils.strToBB("No Engine", defaultCharset));
            vkApplicationInfo.engineVersion(VK_MAKE_VERSION(1, 0, 0));
            vkApplicationInfo.apiVersion(VK_API_VERSION_1_0);

            VkInstanceCreateInfo createInfo = VkInstanceCreateInfo.mallocStack(stack);

            createInfo.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO);
            createInfo.pApplicationInfo(vkApplicationInfo);

            PointerBuffer glfwExtensions = glfwGetRequiredInstanceExtensions();

            int glfwExtensionCount = createInfo.enabledExtensionCount();
            createInfo.ppEnabledExtensionNames(glfwExtensions);
            createInfo.enabledLayerCount();

            PointerBuffer pInstance = stack.mallocPointer(1);
            int err = vkCreateInstance(createInfo, null, pInstance);

            if (err != VK_SUCCESS) {

                throw new AssertionError("Failed to create VkInstance: " + VkUtils.translateVulkanResult(err));
            }

            vkInstance = new VkInstance(pInstance.get(0), createInfo);

            IntBuffer extensionCountBuffer = stack.mallocInt(1);

            vkEnumerateInstanceExtensionProperties((ByteBuffer) null, extensionCountBuffer, null);

            List<VkExtensionProperties> extensions = new ArrayList<>(extensionCountBuffer.get());

        }

    }


    public static void main(String[] args) {
        HelloTriangleApplication helloTriangleApplication = new HelloTriangleApplication();
        helloTriangleApplication.run();
    }
}
