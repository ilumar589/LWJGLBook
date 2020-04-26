import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkClearColorValue;
import org.lwjgl.vulkan.VkClearValue;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;

import static org.lwjgl.system.MemoryUtil.memAllocPointer;
import static org.lwjgl.vulkan.EXTDebugReport.VK_ERROR_VALIDATION_FAILED_EXT;
import static org.lwjgl.vulkan.KHRDisplaySwapchain.VK_ERROR_INCOMPATIBLE_DISPLAY_KHR;
import static org.lwjgl.vulkan.KHRSurface.VK_ERROR_NATIVE_WINDOW_IN_USE_KHR;
import static org.lwjgl.vulkan.KHRSurface.VK_ERROR_SURFACE_LOST_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.VK_ERROR_OUT_OF_DATE_KHR;
import static org.lwjgl.vulkan.KHRSwapchain.VK_SUBOPTIMAL_KHR;
import static org.lwjgl.vulkan.VK10.*;

public class VkUtils {

    public static void vkCheckResult(int err){

        if (err != VK_SUCCESS) {
            throw new AssertionError(translateVulkanResult(err));
        }
    }

    /**
     * Translates a Vulkan {@code VkResult} value to a String describing the result.
     *
     * @param result
     *            the {@code VkResult} value
     *
     * @return the result description
     */
    public static String translateVulkanResult(int result) {
        // Success codes
        // Error codes
        return switch (result) {
            case VK_SUCCESS -> "Command successfully completed.";
            case VK_NOT_READY -> "A fence or query has not yet completed.";
            case VK_TIMEOUT -> "A wait operation has not completed in the specified time.";
            case VK_EVENT_SET -> "An event is signaled.";
            case VK_EVENT_RESET -> "An event is unsignaled.";
            case VK_INCOMPLETE -> "A return array was too small for the result.";
            case VK_SUBOPTIMAL_KHR -> "A swapchain no longer matches the surface properties exactly, but can still be used to present to the surface successfully.";
            case VK_ERROR_OUT_OF_HOST_MEMORY -> "A host memory allocation has failed.";
            case VK_ERROR_OUT_OF_DEVICE_MEMORY -> "A device memory allocation has failed.";
            case VK_ERROR_INITIALIZATION_FAILED -> "Initialization of an object could not be completed for implementation-specific reasons.";
            case VK_ERROR_DEVICE_LOST -> "The logical or physical device has been lost.";
            case VK_ERROR_MEMORY_MAP_FAILED -> "Mapping of a memory object has failed.";
            case VK_ERROR_LAYER_NOT_PRESENT -> "A requested layer is not present or could not be loaded.";
            case VK_ERROR_EXTENSION_NOT_PRESENT -> "A requested extension is not supported.";
            case VK_ERROR_FEATURE_NOT_PRESENT -> "A requested feature is not supported.";
            case VK_ERROR_INCOMPATIBLE_DRIVER -> "The requested version of Vulkan is not supported by the driver or is otherwise incompatible for implementation-specific reasons.";
            case VK_ERROR_TOO_MANY_OBJECTS -> "Too many objects of the type have already been created.";
            case VK_ERROR_FORMAT_NOT_SUPPORTED -> "A requested format is not supported on this device.";
            case VK_ERROR_SURFACE_LOST_KHR -> "A surface is no longer available.";
            case VK_ERROR_NATIVE_WINDOW_IN_USE_KHR -> "The requested window is already connected to a VkSurfaceKHR, or to some other non-Vulkan API.";
            case VK_ERROR_OUT_OF_DATE_KHR -> "A surface has changed in such a way that it is no longer compatible with the swapchain, and further presentation requests using the "
                    + "swapchain will fail. Applications must query the new surface properties and recreate their swapchain if they wish to continue" + "presenting to the surface.";
            case VK_ERROR_INCOMPATIBLE_DISPLAY_KHR -> "The display used by a swapchain does not use the same presentable image layout, or is incompatible in a way that prevents sharing an" + " image.";
            case VK_ERROR_VALIDATION_FAILED_EXT -> "A validation layer found an error.";
            default -> String.format("%s [%d]", "Unknown", result);
        };
    }

    public static PointerBuffer getValidationLayerNames(boolean validation, ByteBuffer[] layers){

        PointerBuffer ppEnabledLayerNames = memAllocPointer(layers.length);
        for (int i = 0; validation && i < layers.length; i++)
            ppEnabledLayerNames.put(layers[i]);
        ppEnabledLayerNames.flip();

        return ppEnabledLayerNames;
    }

    public static VkClearValue getClearValueColor(Vector3f clearColor){

        VkClearValue clearValues = VkClearValue.calloc();
        clearValues.color()
                .float32(0, clearColor.x)
                .float32(1, clearColor.y)
                .float32(2, clearColor.z)
                .float32(3, 1.0f);

        return clearValues;
    }

    public static VkClearColorValue getClearColorValue(){

        VkClearColorValue clearValues = VkClearColorValue.calloc();
        clearValues
                .float32(0, 0.0f)
                .float32(1, 0.0f)
                .float32(2, 0.0f)
                .float32(3, 1.0f);

        return clearValues;
    }

    public static VkClearValue getClearValueDepth(){

        VkClearValue clearValues = VkClearValue.calloc();
        clearValues.depthStencil()
                .depth(1.0f);

        return clearValues;
    }

    public static int getSampleCountBit(int samples){

        int sampleCountBit = switch (samples) {
            case 1 -> VK_SAMPLE_COUNT_1_BIT;
            case 2 -> VK_SAMPLE_COUNT_2_BIT;
            case 4 -> VK_SAMPLE_COUNT_4_BIT;
            case 8 -> VK_SAMPLE_COUNT_8_BIT;
            case 16 -> VK_SAMPLE_COUNT_16_BIT;
            case 32 -> VK_SAMPLE_COUNT_32_BIT;
            case 64 -> VK_SAMPLE_COUNT_64_BIT;
            default -> 0;
        };

        if (sampleCountBit == 0){
            throw new RuntimeException("Multisamplecount: " + samples + ". Allowed numbers [1,2,4,8,16,32,64]");
        }

        return sampleCountBit;
    }

}
