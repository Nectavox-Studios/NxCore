package com.nectavox.nxcore.models.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.nectavox.nxcore.enums.ItemDisplayType;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Builder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Builder
public class ItemDisplayData extends DisplayData {

    private final ItemStack item;

    @Builder.Default
    private final ItemDisplayType type = ItemDisplayType.NONE;

    @Override
    public EntityType getEntityType() {
        return EntityTypes.ITEM_DISPLAY;
    }

    @Override
    public List<EntityData<?>> buildEntityMeta() {
        List<EntityData<?>> meta = new ArrayList<>(buildMeta());

        meta.add(new EntityData<>(
                23,
                EntityDataTypes.ITEMSTACK,
                SpigotConversionUtil.fromBukkitItemStack(item)
        ));

        meta.add(new EntityData<>(
                24,
                EntityDataTypes.BYTE,
                (byte) type.ordinal()
        ));

        return meta;
    }

}
