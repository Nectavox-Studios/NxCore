package com.nectavox.nxcore.utils;


import com.nectavox.nxcore.models.item.CustomModelDataData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;

public final class VersionSupport {

    private VersionSupport() {
    }

    public static void applyItemModel(
            ItemMeta meta,
            String model
    ) {

        if (model == null || model.isBlank()) {
            return;
        }

        try {

            Method method = meta.getClass().getMethod("setItemModel", NamespacedKey.class);
            method.invoke(meta, NamespacedKey.fromString(model));

        } catch (Throwable ignored) {
        }
    }

    public static void applyCustomModelDataComponent(
            ItemMeta meta,
            CustomModelDataData data
    ) {

        if (data == null) {
            return;
        }

        try {

            Method getComponent = meta.getClass().getMethod("getCustomModelDataComponent");
            Object component = getComponent.invoke(meta);

            if (component == null) {
                return;
            }

            applyFloats(component, data.getFloats());
            applyStrings(component, data.getStrings());
            applyFlags(component, data.getFlags());
            applyColors(component, data.getColors());

            Method setComponent = meta.getClass().getMethod("setCustomModelDataComponent", component.getClass());
            setComponent.invoke(meta, component);

        } catch (Throwable ignored) {
        }
    }

    private static void applyFloats(
            Object component,
            List<Float> values
    ) {

        try {

            Method method = component.getClass().getMethod("setFloats", List.class);
            method.invoke(component, values);

        } catch (Throwable ignored) {
        }
    }

    private static void applyStrings(
            Object component,
            List<String> values
    ) {

        try {

            Method method = component.getClass().getMethod("setStrings", List.class);
            method.invoke(component, values);

        } catch (Throwable ignored) {
        }
    }

    private static void applyFlags(
            Object component,
            List<Boolean> values
    ) {

        try {

            Method method = component.getClass().getMethod("setFlags", List.class);
            method.invoke(component, values);

        } catch (Throwable ignored) {
        }
    }

    private static void applyColors(
            Object component,
            List<Integer> values
    ) {

        try {

            Method method = component.getClass().getMethod("setColors", List.class);
            method.invoke(component, values);

        } catch (Throwable ignored) {
        }
    }
}
