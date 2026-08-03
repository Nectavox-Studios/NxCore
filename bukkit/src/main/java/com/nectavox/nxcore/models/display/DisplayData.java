package com.nectavox.nxcore.models.display;

import com.github.retrooper.packetevents.util.Vector3f;
import lombok.Builder;
import org.bukkit.Location;

@Builder
public abstract class DisplayData {
    private final Location location;

    @Builder.Default
    private final Vector3f scale = new Vector3f(1, 1, 1);

    @Builder.Default
    private final Vector3f translation = new Vector3f(0, 0, 0);

    @Builder.Default
    private final byte billboard = 0;

    private final Integer brightness;

    private final Float viewRange;
}
