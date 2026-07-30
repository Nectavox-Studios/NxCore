package com.nectavox.nxcore.commands.models;

import lombok.Value;

@Value
public class CooldownEntry {
    long lastUse;
    int cooldownSeconds;

    public boolean isExpired(long now) {
        return (now - lastUse) >= (cooldownSeconds * 1000L);
    }
}