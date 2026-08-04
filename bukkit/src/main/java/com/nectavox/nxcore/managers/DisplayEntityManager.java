package com.nectavox.nxcore.managers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import com.nectavox.nxcore.NxPlugin;
import com.nectavox.nxcore.models.display.DisplayData;
import com.nectavox.nxcore.models.display.TextDisplayData;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class DisplayEntityManager {
    private final NxPlugin plugin;
    @Getter
    private final List<DisplayData> dataMap = new CopyOnWriteArrayList<>();

    public DisplayEntityManager(NxPlugin plugin) {
        this.plugin = plugin;
        startTrackingTask();
    }

    private void startTrackingTask() {
        plugin.getScheduler().runTimer(task -> {
            for (DisplayData data : dataMap) {
                double viewDistanceSq = data.getViewRange() * data.getViewRange();

                for (Player player : data.getLocation().getWorld().getPlayers()) {
                    double distSq = player.getLocation().distanceSquared(data.getLocation());
                    boolean isSeeing = data.getViewers().contains(player.getUniqueId());

                    if (distSq <= viewDistanceSq) {
                        if (!isSeeing) {
                            spawnFor(player, data);
                        } else if (data instanceof TextDisplayData) {
                            sendMetadata(player, (TextDisplayData) data);
                        }
                    } else if (isSeeing) {
                        destroyFor(player, data);
                    }
                }
            }
        }, 20L, 20L);
    }

    public void createDisplayEntity(DisplayData data) {
        if (dataMap.stream().anyMatch(d -> d.getId() == data.getId())) {
            plugin.getLogger().warning("Duplicate DisplayData ID detected: " + data.getId());
            return;
        }
        dataMap.add(data);
    }

    public void clear() {
        for (DisplayData data : dataMap) {
            for (UUID uuid : data.getViewers()) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    destroyFor(p, data);
                }
            }
            data.getViewers().clear();
        }
        dataMap.clear();
    }

    private void spawnFor(Player player, DisplayData data) {
        data.getViewers().add(player.getUniqueId());

        WrapperPlayServerSpawnEntity spawn = new WrapperPlayServerSpawnEntity(
                data.getId(), data.getUuid(), data.getEntityType(),
                SpigotConversionUtil.fromBukkitLocation(data.getLocation()), 0, 0, null);

        WrapperPlayServerEntityMetadata meta = new WrapperPlayServerEntityMetadata(
                data.getId(), data.buildEntityMeta());

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, spawn);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, meta);
    }

    private void destroyFor(Player player, DisplayData data) {
        data.getViewers().remove(player.getUniqueId());
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerDestroyEntities(data.getId()));
    }

    private void sendMetadata(Player player, TextDisplayData data) {
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerEntityMetadata(data.getId(), data.buildEntityMeta()));
    }

    public DisplayData getDisplay(int id) {
        return dataMap.stream()
                .filter(data -> data.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void updateTextDisplay(int id, List<net.kyori.adventure.text.Component> text) {
        DisplayData display = getDisplay(id);
        if (display == null) {
            plugin.getLogger().warning("Tried to update non-existent hologram with ID: " + id);
            return;
        }

        if (!(display instanceof TextDisplayData)) {
            plugin.getLogger().warning("Tried to update non-TextDisplayData hologram with ID: " + id);
            return;
        }

        TextDisplayData textDisplayData = (TextDisplayData) display;

        List<net.kyori.adventure.text.Component> oldLines = textDisplayData.getLines();
        textDisplayData.setLines(text);

        if (!oldLines.equals(text)) {
            for (UUID viewerUuid : textDisplayData.getViewers()) {
                Player viewer = Bukkit.getPlayer(viewerUuid);
                if (viewer != null) {
                    double viewDistanceSq = textDisplayData.getViewRange() * textDisplayData.getViewRange();
                    if (viewer.getLocation().distanceSquared(textDisplayData.getLocation()) <= viewDistanceSq) {
                        sendMetadata(viewer, textDisplayData);
                    }
                }
            }
        }
    }
}
