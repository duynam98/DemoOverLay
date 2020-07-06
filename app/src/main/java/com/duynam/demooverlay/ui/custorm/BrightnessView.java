package com.duynam.demooverlay.ui.custorm;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;

import androidx.annotation.FloatRange;
import androidx.appcompat.widget.AppCompatImageView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class BrightnessView extends AppCompatImageView {

    private float bright;
    private float contrast;
    private PublishSubject<Float> subject;
    private PublishSubject<Float> subjectContrast;

    public BrightnessView(Context context) {
        super(context);
        initView();
        initContrastView();
    }

    public BrightnessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initContrastView();
    }

    public BrightnessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initContrastView();
    }

    private void initView() {
        subject = PublishSubject.create();
        subject.debounce(0, TimeUnit.MILLISECONDS)
//                .filter(new Predicate<Float>() {
//                    @Override
//                    public boolean test(Float brightness) throws Exception {
//                        return true;
//                    }
//                })
                .distinctUntilChanged()
                .switchMap(new Function<Float, ObservableSource<ColorMatrixColorFilter>>() {
                    @Override
                    public ObservableSource<ColorMatrixColorFilter> apply(Float value) throws Exception {
                        return postBrightness(value);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ColorMatrixColorFilter>() {
                    @Override
                    public void accept(ColorMatrixColorFilter colorMatrixColorFilter) throws Exception {
                        setColorFilter(colorMatrixColorFilter);
                    }
                });
    }

    private void initContrastView() {
        subjectContrast = PublishSubject.create();
        subjectContrast.debounce(0, TimeUnit.MILLISECONDS)
//                .filter(new Predicate<Float>() {
//                    @Override
//                    public boolean test(Float contrast) throws Exception {
//                        return true;
//                    }
//                })
                .distinctUntilChanged()
                .switchMap(new Function<Float, ObservableSource<ColorMatrixColorFilter>>() {
                    @Override
                    public ObservableSource<ColorMatrixColorFilter> apply(Float value) throws Exception {
                        return postContrast(value);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ColorMatrixColorFilter>() {
                    @Override
                    public void accept(ColorMatrixColorFilter colorMatrixColorFilter) throws Exception {
                        setColorFilter(colorMatrixColorFilter);
                    }
                });
    }

    public float getBright() {
        return bright;
    }

    public void setContrast(@FloatRange(from = -180.0f, to = 180.0f) float contrast) {
        this.contrast = contrast / 180.0f + 1.0f;
        subjectContrast.onNext(this.contrast);
    }

    public void setBright(@FloatRange(from = -100, to = 100) float bright) {
        this.bright = bright;
        subject.onNext(this.bright);
    }

    private Observable<ColorMatrixColorFilter> postBrightness(float value) {
        return Observable.just(brightness(value));
    }

    private ColorMatrixColorFilter brightness(float value) {
        ColorMatrix cmB = new ColorMatrix();
        cmB.set(new float[]{
                1, 0, 0, 0, value,
                0, 1, 0, 0, value,
                0, 0, 1, 0, value,
                0, 0, 0, 1, 0});
        return new ColorMatrixColorFilter(cmB);
    }

    private Observable<ColorMatrixColorFilter> postContrast(float value) {
        return Observable.just(contrast(value));
    }

    private ColorMatrixColorFilter contrast(float value) {
        float scale = value;
        float[] array = new float[]{
                scale, 0, 0, 0, 0,
                0, scale, 0, 0, 0,
                0, 0, scale, 0, 0,
                0, 0, 0, 1, 0};
        ColorMatrix matrix = new ColorMatrix(array);
        return new ColorMatrixColorFilter(matrix);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
