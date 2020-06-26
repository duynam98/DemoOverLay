/**
 * Created by Matthew Stewart on 10/30/2017 10:47:00 AM
 */
package com.filter.advanced;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.filter.base.GPUImageFilter;

public class GPUImageVignetteFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = " uniform sampler2D inputImageTexture;\n varying highp vec2 textureCoordinate;\n \n uniform lowp vec2 vignetteCenter;\n uniform lowp vec3 vignetteColor;\n uniform highp float vignetteStart;\n uniform highp float vignetteEnd;\n \n void main()\n {\n     \n     \n     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n }";
    private PointF mVignetteCenter;
    private int mVignetteCenterLocation;
    private float[] mVignetteColor;
    private int mVignetteColorLocation;
    private float mVignetteEnd;
    private int mVignetteEndLocation;
    private float mVignetteStart;
    private int mVignetteStartLocation;

    public GPUImageVignetteFilter() {
        this(new PointF(), new float[]{0.0f, 0.0f, 0.0f}, 0.3f, 0.75f);
    }

    public GPUImageVignetteFilter(PointF vignetteCenter, float[] vignetteColor, float vignetteStart, float vignetteEnd) {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.mVignetteCenter = vignetteCenter;
        this.mVignetteColor = vignetteColor;
        this.mVignetteStart = vignetteStart;
        this.mVignetteEnd = vignetteEnd;
    }

    public void onInit() {
        super.onInit();
        this.mVignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        this.mVignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
        this.mVignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        this.mVignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
        setVignetteCenter(this.mVignetteCenter);
        setVignetteColor(this.mVignetteColor);
        setVignetteStart(this.mVignetteStart);
        setVignetteEnd(this.mVignetteEnd);
    }

    public void setVignetteCenter(PointF vignetteCenter) {
        this.mVignetteCenter = vignetteCenter;
        setPoint(this.mVignetteCenterLocation, this.mVignetteCenter);
    }

    public void setVignetteColor(float[] vignetteColor) {
        this.mVignetteColor = vignetteColor;
        setFloatVec3(this.mVignetteColorLocation, this.mVignetteColor);
    }

    public void setVignetteStart(float vignetteStart) {
        this.mVignetteStart = vignetteStart;
        setFloat(this.mVignetteStartLocation, this.mVignetteStart);
    }

    public void setVignetteEnd(float vignetteEnd) {
        this.mVignetteEnd = vignetteEnd;
        setFloat(this.mVignetteEndLocation, this.mVignetteEnd);
    }
}
