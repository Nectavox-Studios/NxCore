package com.nectavox.nxcore.utils;

import com.nectavox.nxcore.models.item.CustomModelDataData;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

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
            Method method = ItemMeta.class.getMethod(
                    "setItemModel",
                    NamespacedKey.class
            );

            NamespacedKey key = NamespacedKey.fromString(model);

            if (key != null) {
                method.invoke(meta, key);
            }

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
            Method method = ItemMeta.class.getMethod(
                    "setTooltipStyle",
                    NamespacedKey.class
            );

            NamespacedKey key = NamespacedKey.fromString(tooltipStyle);

            if (key != null) {
                method.invoke(meta, key);
            }

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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
            Method method = ItemMeta.class.getMethod(
                    "setHideTooltip",
                    boolean.class
            );

            method.invoke(meta, true);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

            Method method = ItemMeta.class.getMethod(
                    "setRarity",
                    rarityClass
            );

            method.invoke(meta, rarityValue);

        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

            Method getComponent = ItemMeta.class.getMethod(
                    "getCustomModelDataComponent"
            );

            CustomModelDataComponent component =
                    (CustomModelDataComponent) getComponent.invoke(meta);

            if (component == null) {
                return;
            }

            applyFloats(component, data.getFloats());
            applyStrings(component, data.getStrings());
            applyFlags(component, data.getFlags());
            applyColors(component, data.getColors());

            Method setComponent = ItemMeta.class.getMethod(
                    "setCustomModelDataComponent",
                    CustomModelDataComponent.class
            );

            setComponent.invoke(meta, component);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private static void applyFloats(
            CustomModelDataComponent component,
            List<Float> values
    ) {

        if (values == null || values.isEmpty()) {
            return;
        }

        try {

            Method method = CustomModelDataComponent.class.getMethod(
                    "setFloats",
                    List.class
            );

            method.invoke(component, values);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private static void applyStrings(
            CustomModelDataComponent component,
            List<String> values
    ) {

        if (values == null || values.isEmpty()) {
            return;
        }

        try {

            Method method = CustomModelDataComponent.class.getMethod(
                    "setStrings",
                    List.class
            );

            method.invoke(component, values);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private static void applyFlags(
            CustomModelDataComponent component,
            List<Boolean> values
    ) {

        if (values == null || values.isEmpty()) {
            return;
        }

        try {

            Method method = CustomModelDataComponent.class.getMethod(
                    "setFlags",
                    List.class
            );

            method.invoke(component, values);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }


    private static void applyColors(
            CustomModelDataComponent component,
            List<Integer> values
    ) {

        if (values == null || values.isEmpty()) {
            return;
        }

        try {

            Method method = CustomModelDataComponent.class.getMethod(
                    "setColors",
                    List.class
            );

            method.invoke(component, values);

        } catch (NoSuchMethodException ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
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

                ItemFlag flag = ItemFlag.valueOf(
                        name.toUpperCase()
                );

                meta.addItemFlags(flag);

            } catch (IllegalArgumentException ignored) {
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }


    private static boolean applyModernItemFlags(
            ItemMeta meta,
            Set<String> flags
    ) {

        try {

            Method getTooltipDisplay = ItemMeta.class.getMethod(
                    "getTooltipDisplay"
            );

            Object tooltipDisplay = getTooltipDisplay.invoke(meta);

            if (tooltipDisplay == null) {
                return false;
            }

            Class<?> tooltipDisplayClass =
                    getTooltipDisplay.getReturnType();

            Method setHiddenComponents = tooltipDisplayClass.getMethod(
                    "setHiddenComponents",
                    List.class
            );

            setHiddenComponents.invoke(
                    tooltipDisplay,
                    List.copyOf(flags)
            );

            Method setTooltipDisplay = ItemMeta.class.getMethod(
                    "setTooltipDisplay",
                    tooltipDisplayClass
            );

            setTooltipDisplay.invoke(
                    meta,
                    tooltipDisplay
            );

            return true;

        } catch (NoSuchMethodException ignored) {
            return false;

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }
}