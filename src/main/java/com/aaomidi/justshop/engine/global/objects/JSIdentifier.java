package com.aaomidi.justshop.engine.global.objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class JSIdentifier {
    @Getter
    private final Material material;
    @Getter
    private final Integer durability;
}
