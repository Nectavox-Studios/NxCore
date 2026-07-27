package com.nectavox.nxcore.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public class MessageUtil {
    public static void sendMessage(Object sender, Component message) {
        if (sender instanceof Audience audience) {
            audience.sendMessage(message);
        }
    }

    public static void sendActionBar(Object sender, Component message) {
        if (sender instanceof Audience audience) {
            audience.sendActionBar(message);
        }
    }
}
