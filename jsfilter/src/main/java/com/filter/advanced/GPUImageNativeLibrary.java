/**
 * Created by Matthew Stewart on 10/30/2017 10:46:59 AM
 */


package com.filter.advanced;

public class GPUImageNativeLibrary {
    static {
        System.loadLibrary("gpuimage-library");
    }

    public static native void YUVtoRBGA(byte[] yuv, int width, int height, int[] out);

    public static native void YUVtoARBG(byte[] yuv, int width, int height, int[] out);
}
