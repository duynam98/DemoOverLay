package com.xiaopo.flying.sticker;

public class ItemSticker {
    private Sticker sticker;
    private int alpha;

    public ItemSticker(Sticker sticker, int alpha) {
        this.sticker = sticker;
        this.alpha = alpha;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }
}
