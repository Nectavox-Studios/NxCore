package com.nectavox.nxcore.models.display;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.nectavox.nxcore.NxPlugin;
import com.nectavox.nxcore.enums.TextAlignment;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
public class TextDisplayData extends DisplayData {

    @Setter
    @Getter
    private List<Component> lines;

    @Builder.Default
    private final int lineWidth = 200;

    @Builder.Default
    private final int background = 0x00000000;

    @Builder.Default
    private final byte textOpacity = -1;

    @Builder.Default
    private final boolean textShadow = false;

    @Builder.Default
    private final boolean seeThrough = false;

    @Builder.Default
    private final boolean useDefaultBGColor = false;

    @Builder.Default
    private final TextAlignment alignment = TextAlignment.CENTER;

    @Override
    public EntityType getEntityType() {
        return EntityTypes.TEXT_DISPLAY;
    }

    @Override
    public List<EntityData<?>> buildEntityMeta() {
        List<EntityData<?>> meta = new ArrayList<>(buildMeta());

        Component text = Component.empty();

        for (int i = 0; i < lines.size(); i++) {
            text = text.append(lines.get(i));
            if (i + 1 < lines.size())
                text = text.append(Component.newline());
        }

        meta.add(new EntityData<>(
                23,
                EntityDataTypes.ADV_COMPONENT,
                text
        ));

        meta.add(new EntityData<>(
                24,
                EntityDataTypes.INT,
                this.lineWidth
        ));

        meta.add(new EntityData<>(
                25,
                EntityDataTypes.INT,
                this.background
        ));


        meta.add(new EntityData<>(
                26,
                EntityDataTypes.BYTE,
                textOpacity
        ));

        byte flags = 0;

        if (textShadow) flags |= 0x01;
        if (seeThrough) flags |= 0x02;
        if (useDefaultBGColor) flags |= 0x04;

        int alignValue = alignment.ordinal();
        flags |= (alignValue << 3);

        meta.add(new EntityData<>(
                27,
                EntityDataTypes.BYTE,
                flags
        ));

        return meta;
    }
}
