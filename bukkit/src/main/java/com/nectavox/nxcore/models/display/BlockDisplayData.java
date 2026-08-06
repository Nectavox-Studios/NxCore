package com.nectavox.nxcore.models.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
public class BlockDisplayData extends DisplayData {

    private final BlockData block;

    @Override
    public EntityType getEntityType() {
        return EntityTypes.BLOCK_DISPLAY;
    }

    @Override
    public List<EntityData<?>> buildEntityMeta() {
        List<EntityData<?>> meta = new ArrayList<>(buildMeta());

        meta.add(new EntityData<>(
                23,
                EntityDataTypes.BLOCK_STATE,
                SpigotConversionUtil.fromBukkitBlockData(block).getGlobalId()
        ));

        return meta;
    }

}
