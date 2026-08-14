package com.nectavox.nxcore.utils;

import com.nectavox.nxcore.models.item.CustomModelDataData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

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
            Method method = meta.getClass()
                    .getMethod("setItemModel", NamespacedKey.class);

            NamespacedKey key = NamespacedKey.fromString(model);

            if (key != null) {
                method.invoke(meta, key);
            }

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    public static void applyTooltipStyle(
            ItemMeta meta,
            String tooltipStyle
    ) {

        if (tooltipStyle == null || tooltipStyle.isBlank()) {
            return;
        }

        try {
            Method method = meta.getClass()
                    .getMethod("setTooltipStyle", NamespacedKey.class);

            NamespacedKey key = NamespacedKey.fromString(tooltipStyle);

            if (key != null) {
                method.invoke(meta, key);
            }

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    public static void applyHideTooltip(
            ItemMeta meta,
            boolean hideTooltip
    ) {

        if (!hideTooltip) {
            return;
        }

        try {
            Method method = meta.getClass()
                    .getMethod("setHideTooltip", boolean.class);

            method.invoke(meta, true);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    public static void applyRarity(
            ItemMeta meta,
            String rarity
    ) {

        if (rarity == null || rarity.isBlank()) {
            return;
        }

        try {

            Class<?> rarityClass = Class.forName(
                    "org.bukkit.inventory.ItemRarity"
            );

            Object rarityValue = Enum.valueOf(
                    (Class<? extends Enum>) rarityClass.asSubclass(Enum.class),
                    rarity.toUpperCase()
            );

            Method method = meta.getClass()
                    .getMethod("setRarity", rarityClass);

            method.invoke(meta, rarityValue);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
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

            Method getComponent = meta.getClass()
                    .getMethod("getCustomModelDataComponent");

            Object component = getComponent.invoke(meta);

            if (component == null) {
                return;
            }

            applyFloats(component, data.getFloats());
            applyStrings(component, data.getStrings());
            applyFlags(component, data.getFlags());
            applyColors(component, data.getColors());

            Method setComponent = meta.getClass()
                    .getMethod(
                            "setCustomModelDataComponent",
                            component.getClass()
                    );

            setComponent.invoke(meta, component);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    private static void applyFloats(
            Object component,
            List<Float> values
    ) {

        try {

            Method method = component.getClass()
                    .getMethod("setFloats", List.class);

            method.invoke(component, values);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    private static void applyStrings(
            Object component,
            List<String> values
    ) {

        try {

            Method method = component.getClass()
                    .getMethod("setStrings", List.class);

            method.invoke(component, values);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    private static void applyFlags(
            Object component,
            List<Boolean> values
    ) {

        try {

            Method method = component.getClass()
                    .getMethod("setFlags", List.class);

            method.invoke(component, values);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }


    private static void applyColors(
            Object component,
            List<Integer> values
    ) {

        try {

            Method method = component.getClass()
                    .getMethod("setColors", List.class);

            method.invoke(component, values);

        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }
    }

    public static void applyItemFlags(
            ItemMeta meta,
            Set<String> flags
    ) {
        if (flags == null || flags.isEmpty()) {
            return;
        }

        if (applyModernItemFlags(meta, flags)) {
            return;
        }

        applyLegacyItemFlags(meta, flags);
    }

    private static void applyLegacyItemFlags(
            ItemMeta meta,
            Set<String> flags
    ) {
        for (String name : flags) {
            try {
                ItemFlag flag = ItemFlag.valueOf(name.toUpperCase());

                meta.addItemFlags(flag);

            } catch (IllegalArgumentException ignored) {
            } catch (Throwable ignored) {
                ignored.printStackTrace();
            }
        }
    }

    private static boolean applyModernItemFlags(
            ItemMeta meta,
            Set<String> flags
    ) {
        try {

            Method getTooltipDisplay = findMethod(
                    meta.getClass(),
                    "getTooltipDisplay"
            );

            if (getTooltipDisplay == null) {
                return false;
            }

            Object tooltipDisplay = getTooltipDisplay.invoke(meta);

            if (tooltipDisplay == null) {
                return false;
            }

            Method setHiddenComponents = findMethod(
                    tooltipDisplay.getClass(),
                    "setHiddenComponents",
                    List.class
            );

            if (setHiddenComponents == null) {
                return false;
            }

            setHiddenComponents.invoke(
                    tooltipDisplay,
                    List.copyOf(flags)
            );

            Method setTooltipDisplay = findMethod(
                    meta.getClass(),
                    "setTooltipDisplay",
                    tooltipDisplay.getClass()
            );

            if (setTooltipDisplay == null) {
                return false;
            }

            setTooltipDisplay.invoke(
                    meta,
                    tooltipDisplay
            );

            return true;

        } catch (Throwable ignored) {
            ignored.printStackTrace();
            return false;
        }
    }

    private static Method findMethod(
            Class<?> clazz,
            String name,
            Class<?>... parameterTypes
    ) {
        try {
            return clazz.getMethod(name, parameterTypes);
        } catch (Throwable ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

}