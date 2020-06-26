/**
 * Created by Matthew Stewart on 10/30/2017 10:47:00 AM
 */
package com.filter.advanced;

import android.content.Context;
import android.opengl.GLES20;

import com.filter.R;
import com.filter.base.GPUImageFilter;
import com.filter.base.OpenGlUtils;
import com.filter.helper.FilterManager;

public class MagicFreudFilter extends GPUImageFilter {
    private int mTexelHeightUniformLocation;
    private int mTexelWidthUniformLocation;
    private int[] inputTextureHandles = {-1};
    private int[] inputTextureUniformLocations = {-1};
    private int mGLStrengthLocation;
    private Context context;

    public MagicFreudFilter() {
        super(NO_FILTER_VERTEX_SHADER, OpenGlUtils.readShaderFromRawResource(R.raw.freud));
        this.context =  FilterManager.getContext();
    }

    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, inputTextureHandles, 0);
        for (int i = 0; i < inputTextureHandles.length; i++)
            inputTextureHandles[i] = -1;
    }

    protected void onDrawArraysAfter() {
        for (int i = 0; i < inputTextureHandles.length
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre() {
        for (int i = 0; i < inputTextureHandles.length
                && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE; i++) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3));
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i]);
            GLES20.glUniform1i(inputTextureUniformLocations[i], (i + 3));
        }
    }

    public void onInit() {
        super.onInit();
        inputTextureUniformLocations[0] = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");

        mTexelWidthUniformLocation = GLES20.glGetUniformLocation(getProgram(), "inputImageTextureWidth");
        mTexelHeightUniformLocation = GLES20.glGetUniformLocation(getProgram(), "inputImageTextureHeight");

        mGLStrengthLocation = GLES20.glGetUniformLocation(mGLProgId,
                "strength");
    }

    public void onInitialized() {
        super.onInitialized();
        setFloat(mGLStrengthLocation, 1.0f);
        runOnDraw(new Runnable() {
            public void run() {
                inputTextureHandles[0] = OpenGlUtils.loadTexture(context, "filter/freud_rand.png");
            }
        });
    }

    public void onInputSizeChanged(int width, int height) {
        super.onInputSizeChanged(width, height);
        GLES20.glUniform1f(mTexelWidthUniformLocation, (float) width);
        GLES20.glUniform1f(mTexelHeightUniformLocation, (float) height);
    }
}
