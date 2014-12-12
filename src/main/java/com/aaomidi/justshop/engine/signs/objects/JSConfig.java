package com.aaomidi.justshop.engine.signs.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Created by aaomidi on 11/12/2014.
 */
@RequiredArgsConstructor
public class JSConfig {
    @Getter
    private final File file;
    @Getter
    private final FileConfiguration config;

    public boolean save() {
        try {
            config.save(file);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean load() {
        try {
            config.load(file);
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
