/**
 * Created by Matthew Stewart on 10/30/2017 10:47:01 AM
 */


package com.filter.advanced.adjust;

import android.opengl.GLES20;

import com.filter.base.GPUImageFilter;

public class GPUImageExposureFilter extends GPUImageFilter {
    public static final String EXPOSURE_FRAGMENT_SHADER = "" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform highp float exposure;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     \n" +
            "     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n" +
            " } ";

    private int mExposureLocation;
    private float mExposure;

    public GPUImageExposureFilter() {
        this(0.0f);
    }

    public GPUImageExposureFilter(final float exposure) {
        super(NO_FILTER_VERTEX_SHADER, EXPOSURE_FRAGMENT_SHADER);
        mExposure = exposure;
    }

    @Override
    public void onInit() {
        super.onInit();
        mExposureLocation = GLES20.glGetUniformLocation(getProgram(), "exposure");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setExposure(mExposure);
    }

    public void setExposure(final float exposure) {
        mExposure = exposure;
        setFloat(mExposureLocation, mExposure);
    }
}
