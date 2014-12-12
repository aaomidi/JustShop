package com.aaomidi.justshop.engine.signs.objects;

import com.aaomidi.justshop.engine.global.objects.JSIdentifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author aaomidi
 */
@RequiredArgsConstructor
public class SignCache {
    @Getter
    private HashMap<UUID, JSSign> uuidjsSignMap = new HashMap<>();
    @Getter
    private HashMap<JSIdentifier, JSSign> jsIdentifierJSSignMap = new HashMap<>();
    @Getter
    private HashMap<JSLocation, JSSign> locationJSSignMap = new HashMap<>();

    public void add(JSSign jsSign) {
        uuidjsSignMap.put(jsSign.getUuid(), jsSign);
        jsIdentifierJSSignMap.put(new JSIdentifier(jsSign.getItem().getIcon(), (int) jsSign.getItem().getDurability()), jsSign);
        locationJSSignMap.put(jsSign.getLocation(), jsSign);
    }

    public JSSign getSignAtLocation(Location loc) {
        return locationJSSignMap.get(new JSLocation().fromLocation(loc));
    }
}
