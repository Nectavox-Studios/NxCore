package com.nectavox.nxcore.models.gui;

import lombok.Value;

import java.util.Map;

@Value
public class GuiData {
    String title;
    int rows;
    int itemsPerPage;
    Map<String, GuiItemData> items;

    public GuiItemData getItem(String key) {
        return items.get(key);
    }
}