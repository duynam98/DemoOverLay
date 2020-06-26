/**
 * Created by Matthew Stewart on 10/30/2017 10:46:59 AM
 */
package com.filter.helper;


import android.content.Context;

import com.filter.R;
import com.filter.advanced.JSNormalFilter;
import com.filter.advanced.JSToneCurved;
import com.filter.base.GPUImageFilter;


public class FilterManager {

    private static FilterManager instance;
    private static Context context;

    private FilterManager() {
    }

    public static void init(Context ct) {
        context = ct;
        instance = new FilterManager();
    }

    public static FilterManager getInstance() {
        return instance;
    }

    public static Context getContext() {
        return context;
    }

    public MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.AFTERGLOW,
            MagicFilterType.ALICE_IN_WONDERLAND,
            MagicFilterType.AMBERS,
            MagicFilterType.AUGUST_MARCH,
            MagicFilterType.AURORA,
            MagicFilterType.BABY_FACE,
            MagicFilterType.BLOOD_ORANGE,
            MagicFilterType.BLUE_POPPIES,
            MagicFilterType.BLUE_YELLOW_FIELD,
            MagicFilterType.CAROUSEL,
            MagicFilterType.COLD_DESERT,
            MagicFilterType.COLD_HEART,
            MagicFilterType.COUNTRY,
            MagicFilterType.DIGITAL_FILM,
            MagicFilterType.DOCUMENTARY,
            MagicFilterType.FOGY_BLUE,
            MagicFilterType.FRESH_BLUE,
            MagicFilterType.GHOSTS_IN_YOUR_HEADY,
            MagicFilterType.GOLDEN_HOUR,
            MagicFilterType.GOOD_LUCK_CHARM,
            MagicFilterType.GREEN_ENVY,
            MagicFilterType.HUMMING_BIRDS,
            MagicFilterType.KISS_KISS,
            MagicFilterType.LEFT_HAND_BLUES,
            MagicFilterType.LIGHT_PARADES,
            MagicFilterType.LULLABYE,
            MagicFilterType.MOTH_WINGS,
            MagicFilterType.MYSTERY,
            MagicFilterType.OLD_POSTCARDS,
            MagicFilterType.PEACOCK_FEATHERS,
            MagicFilterType.PISTOL,
            MagicFilterType.RAGDOLL,
            MagicFilterType.ROSE_THORNS_TWO,
            MagicFilterType.SNOW_WHITE,
            MagicFilterType.SPARKS,
            MagicFilterType.TOES_IN_THE_OCEAN,
            MagicFilterType.TONE_LEMON,
            MagicFilterType.WILD_AT_HEART,
            MagicFilterType.WINDOW_WARMTH
    };

    public GPUImageFilter getFilter(MagicFilterType type) {
        JSToneCurved jsToneCurved = new JSToneCurved();
        switch (type) {
            case AFTERGLOW:
                jsToneCurved.setCurveFile(R.raw.afterglow);
                return jsToneCurved;
            case ALICE_IN_WONDERLAND:
                jsToneCurved.setCurveFile(R.raw.alice_in_wonderland);
                return jsToneCurved;
            case AMBERS:
                jsToneCurved.setCurveFile(R.raw.ambers);
                return jsToneCurved;
            case AUGUST_MARCH:
                jsToneCurved.setCurveFile(R.raw.august_march);
                return jsToneCurved;
            case AURORA:
                jsToneCurved.setCurveFile(R.raw.aurora);
                return jsToneCurved;
            case BABY_FACE:
                jsToneCurved.setCurveFile(R.raw.baby_face);
                return jsToneCurved;
            case BLOOD_ORANGE:
                jsToneCurved.setCurveFile(R.raw.blood_orange);
                return jsToneCurved;
            case BLUE_POPPIES:
                jsToneCurved.setCurveFile(R.raw.blue_poppies);
                return jsToneCurved;
            case BLUE_YELLOW_FIELD:
                jsToneCurved.setCurveFile(R.raw.blue_yellow_field);
                return jsToneCurved;
            case CAROUSEL:
                jsToneCurved.setCurveFile(R.raw.carousel);
                return jsToneCurved;
            case COLD_DESERT:
                jsToneCurved.setCurveFile(R.raw.cold_desert);
                return jsToneCurved;
            case COLD_HEART:
                jsToneCurved.setCurveFile(R.raw.cold_heart);
                return jsToneCurved;
            case COUNTRY:
                jsToneCurved.setCurveFile(R.raw.country);
                return jsToneCurved;
            case DIGITAL_FILM:
                jsToneCurved.setCurveFile(R.raw.digital_film);
                return jsToneCurved;
            case DOCUMENTARY:
                jsToneCurved.setCurveFile(R.raw.documentary);
                return jsToneCurved;
            case FOGY_BLUE:
                jsToneCurved.setCurveFile(R.raw.fogy_blue);
                return jsToneCurved;
            case FRESH_BLUE:
                jsToneCurved.setCurveFile(R.raw.fresh_blue);
                return jsToneCurved;
            case GHOSTS_IN_YOUR_HEADY:
                jsToneCurved.setCurveFile(R.raw.ghosts_in_your_head);
                return jsToneCurved;
            case GOLDEN_HOUR:
                jsToneCurved.setCurveFile(R.raw.golden_hour);
                return jsToneCurved;
            case GOOD_LUCK_CHARM:
                jsToneCurved.setCurveFile(R.raw.good_luck_charm);
                return jsToneCurved;
            case GREEN_ENVY:
                jsToneCurved.setCurveFile(R.raw.green_envy);
                return jsToneCurved;
            case HUMMING_BIRDS:
                jsToneCurved.setCurveFile((R.raw.humming_birds));
                return jsToneCurved;
            case KISS_KISS:
                jsToneCurved.setCurveFile((R.raw.kiss_kiss));
                return jsToneCurved;
            case LEFT_HAND_BLUES:
                jsToneCurved.setCurveFile((R.raw.left_hand_blues));
                return jsToneCurved;
            case LIGHT_PARADES:
                jsToneCurved.setCurveFile((R.raw.light_parades));
                return jsToneCurved;
            case LULLABYE:
                jsToneCurved.setCurveFile((R.raw.lullabye));
                return jsToneCurved;
            case MOTH_WINGS:
                jsToneCurved.setCurveFile((R.raw.moth_wings));
                return jsToneCurved;
            case MYSTERY:
                jsToneCurved.setCurveFile((R.raw.mystery));
                return jsToneCurved;
            case OLD_POSTCARDS:
                jsToneCurved.setCurveFile((R.raw.old_postcards));
                return jsToneCurved;
            case PEACOCK_FEATHERS:
                jsToneCurved.setCurveFile((R.raw.peacock_feathers));
                return jsToneCurved;
            case PISTOL:
                jsToneCurved.setCurveFile((R.raw.pistol));
                return jsToneCurved;
            case RAGDOLL:
                jsToneCurved.setCurveFile((R.raw.ragdoll));
                return jsToneCurved;
            case ROSE_THORNS_TWO:
                jsToneCurved.setCurveFile((R.raw.rose_thorns_two));
                return jsToneCurved;
            case SNOW_WHITE:
                jsToneCurved.setCurveFile((R.raw.snow_white));
                return jsToneCurved;
            case SPARKS:
                jsToneCurved.setCurveFile((R.raw.sparks));
                return jsToneCurved;
            case TOES_IN_THE_OCEAN:
                jsToneCurved.setCurveFile((R.raw.toes_in_the_ocean));
                return jsToneCurved;
            case TONE_LEMON:
                jsToneCurved.setCurveFile((R.raw.tone_lemon));
                return jsToneCurved;
            case WILD_AT_HEART:
                jsToneCurved.setCurveFile((R.raw.wild_at_heart));
                return jsToneCurved;
            case WINDOW_WARMTH:
                jsToneCurved.setCurveFile((R.raw.window_warmth));
                return jsToneCurved;
            default:
                return new JSNormalFilter();
        }
    }

    public int getFilterName(MagicFilterType filterType) {
        switch (filterType) {
            case AFTERGLOW:
                return R.string.afterglow;
            case ALICE_IN_WONDERLAND:
                return R.string.alice_in_wonderland;
            case AMBERS:
                return R.string.ambers;
            case AUGUST_MARCH:
                return R.string.august_march;
            case AURORA:
                return R.string.aurora;
            case BABY_FACE:
                return R.string.baby_face;
            case BLOOD_ORANGE:
                return R.string.blood_orange;
            case BLUE_POPPIES:
                return R.string.blue_poppies;
            case BLUE_YELLOW_FIELD:
                return R.string.blue_yellow_field;
            case CAROUSEL:
                return R.string.carousel;
            case COLD_DESERT:
                return R.string.cold_desert;
            case COLD_HEART:
                return R.string.cold_heart;
            case COUNTRY:
                return R.string.country;
            case DIGITAL_FILM:
                return R.string.digital_film;
            case DOCUMENTARY:
                return R.string.documentary;
            case FOGY_BLUE:
                return R.string.fogy_blue;
            case FRESH_BLUE:
                return R.string.fresh_blue;
            case GHOSTS_IN_YOUR_HEADY:
                return R.string.ghosts_in_your_heady;
            case GOLDEN_HOUR:
                return R.string.golden_hour;
            case GOOD_LUCK_CHARM:
                return R.string.good_luck_charm;
            case GREEN_ENVY:
                return R.string.green_envy;
            case HUMMING_BIRDS:
                return R.string.humming_birds;
            case KISS_KISS:
                return R.string.kiss_kiss;
            case LEFT_HAND_BLUES:
                return R.string.left_hand_blues;
            case LIGHT_PARADES:
                return R.string.light_parades;
            case LULLABYE:
                return R.string.lullabye;
            case MOTH_WINGS:
                return R.string.moth_wings;
            case MYSTERY:
                return R.string.mystery;
            case OLD_POSTCARDS:
                return R.string.old_postcards;
            case PEACOCK_FEATHERS:
                return R.string.peacock_feathers;
            case PISTOL:
                return R.string.pistol;
            case RAGDOLL:
                return R.string.ragdoll;
            case ROSE_THORNS_TWO:
                return R.string.rose_thorns_two;
            case SNOW_WHITE:
                return R.string.snow_white;
            case SPARKS:
                return R.string.sparks;
            case TOES_IN_THE_OCEAN:
                return R.string.toes_in_the_ocean;
            case TONE_LEMON:
                return R.string.tone_lemon;
            case WILD_AT_HEART:
                return R.string.wild_at_heart;
            case WINDOW_WARMTH:
                return R.string.window_warmth;
            default:
                return R.string.filter_normal;
        }
    }
}
