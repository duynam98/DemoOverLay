/**
 * Created by Matthew Stewart on 10/30/2017 10:47:00 AM
 */
package com.filter.advanced;

import com.filter.base.GPUImageFilter;

public class JSNormalFilter extends GPUImageFilter {

    public static final String SHADER = "precision lowp float;\n" +
            " precision lowp float;\n" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            "\n" +
            " void main()\n" +
            " {\n" +
            "     \n" +
            "     vec3 texel = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     \n" +
            "     gl_FragColor = vec4(texel, 1.0);\n" +
            " }";

    public JSNormalFilter() {
        super(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);//NO_FILTER_VERTEX_SHADER, SHADER);
    }

}
