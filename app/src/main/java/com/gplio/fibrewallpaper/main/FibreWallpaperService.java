package com.gplio.fibrewallpaper.main;

import glwallpaper.GLWallpaperService;

/**
 * Created by goncalopalaio on 14/07/18.
 */

public class FibreWallpaperService extends GLWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new MainEngine();
    }

    private class MainEngine extends GLEngine {
        MainEngine() {
            setRenderer(new MainRenderer());
        }
    }
}
