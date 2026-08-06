package com.nectavox.nxcore.models.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.nectavox.nxcore.enums.BillBoardType;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Builder
@Getter
public abstract class DisplayData {
    private final Location location;

    @Getter
    private final Set<UUID> viewers = ConcurrentHashMap.newKeySet();

    @Builder.Default
    private final Vector3f scale = new Vector3f(1, 1, 1);

    @Builder.Default
    private final Vector3f translation = new Vector3f(0, 0, 0);

    @Builder.Default
    private final Quaternion4f rotateLeft = new Quaternion4f(0, 0, 0, 0);

    @Builder.Default
    private final Quaternion4f rotateRight = new Quaternion4f(0, 0, 0, 0);

    @Builder.Default
    private final BillBoardType billboard = BillBoardType.FIXED;

    @Builder.Default
    private final Integer brightness = -1;

    @Builder.Default
    private final Float viewRange = 1.0f;

    @Builder.Default
    private final Float shadowRadius = 0.0f;

    @Builder.Default
    private final Float shadowStrength = 1.0f;

    @Builder.Default
    private final Float width = 0.0f;

    @Builder.Default
    private final Float height = 0.0f;

    @Builder.Default
    private final int id = SpigotReflectionUtil.generateEntityId();;
    @Builder.Default
    private final UUID uuid = UUID.randomUUID();;

    List<EntityData<?>> buildMeta() {
        List<EntityData<?>> meta = new ArrayList<>();

        meta.add(new EntityData<>(
                12,
                EntityDataTypes.VECTOR3F,
                this.scale
        ));

        meta.add(new EntityData<>(
                11,
                EntityDataTypes.VECTOR3F,
                this.translation
        ));

        meta.add(new EntityData<>(
                13,
                EntityDataTypes.QUATERNION,
                this.rotateLeft
        ));

        meta.add(new EntityData<>(
                14,
                EntityDataTypes.QUATERNION,
                this.rotateRight
        ));

        meta.add(new EntityData<>(
                15,
                EntityDataTypes.BYTE,
                (byte) this.billboard.ordinal()
        ));

        meta.add(new EntityData<>(
                16,
                EntityDataTypes.INT,
                this.brightness
        ));

        meta.add(new EntityData<>(
                17,
                EntityDataTypes.FLOAT,
                this.viewRange
        ));

        meta.add(new EntityData<>(
                18,
                EntityDataTypes.FLOAT,
                this.shadowRadius
        ));

        meta.add(new EntityData<>(
                19,
                EntityDataTypes.FLOAT,
                this.shadowStrength
        ));

        meta.add(new EntityData<>(
                20,
                EntityDataTypes.FLOAT,
                this.width
        ));

        meta.add(new EntityData<>(
                21,
                EntityDataTypes.FLOAT,
                this.height
        ));

        return meta;
    }

    public List<EntityData<?>> buildEntityMeta() {
        return List.of();
    }

    public EntityType getEntityType() {
        return null;
    }

}
