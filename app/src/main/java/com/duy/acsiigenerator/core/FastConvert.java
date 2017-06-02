package com.duy.acsiigenerator.core;

import android.content.res.AssetManager;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Duy on 06-May-17.
 */

public class FastConvert {
    /**
     * this maps wil be store {@link FigletFont} created
     */
    private HashMap<String, FigletFont> caches = new HashMap<>();

    private AssetManager assetManager;

    public FastConvert(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public String convert(String fontName, String data) {
        if (caches.get(fontName) != null) {
            return caches.get(fontName).convert(data);
        } else {
            try {
                caches.put(fontName, new FigletFont(assetManager.open("fonts/" + fontName)));
                return caches.get(fontName).convert(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
