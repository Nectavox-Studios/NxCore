package com.nectavox.nxcore.sign;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.world.TileEntityType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockEntityData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenSignEditor;
import com.nectavox.nxcore.utils.Color;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class SignUtil {

    @Getter
    private static final SignUtil instance = new SignUtil();
    private final Map<UUID, PendingInput> pendingInputs = new ConcurrentHashMap<>();

    public void open(Player player, List<String> lines, Consumer<String> callback, JavaPlugin plugin) {

        String[] signLines = new String[]{"", "", "", ""};

        if (lines != null) {
            for (int i = 0; i < Math.min(4, lines.size()); i++) {
                signLines[i] = Color.colorLegacy(lines.get(i));
            }
        }

        Location location = player.getLocation().getBlock().getLocation();
        BlockData realOldData =
                location.getBlock().getBlockData().clone();

        BlockData fakeSignData =
                Material.OAK_SIGN.createBlockData();

        Vector3i pos =
                SpigotConversionUtil
                        .fromBukkitLocation(location)
                        .getPosition()
                        .toVector3i();

        sendBlockChange(player, pos, fakeSignData);

        pendingInputs.put(
                player.getUniqueId(),
                new PendingInput(
                        callback,
                        location,
                        realOldData,
                        player.getUniqueId()
                )
        );

        player.getScheduler().runDelayed(
                plugin,
                task -> {

                    sendSignNbt(player, pos, signLines);

                    sendOpenSignEditor(player, pos);

                },
                null,
                1L
        );

        player.getScheduler().runDelayed(
                plugin,
                task -> cleanup(player.getUniqueId()),
                null,
                20L * 120L
        );
    }

    public PendingInput remove(UUID playerId) {
        return pendingInputs.remove(playerId);
    }

    public boolean isProtected(Location location) {
        return pendingInputs.values()
                .stream()
                .anyMatch(input -> input.matches(location));
    }

    public boolean isProtectedSupport(Location location) {
        return pendingInputs.values()
                .stream()
                .anyMatch(input -> input.matchesSupport(location));
    }

    public void cleanup(UUID playerId) {
        PendingInput input = pendingInputs.remove(playerId);

        if (input == null) {
            return;
        }

        input.restore();
    }

    public PendingInput findAndRemove(Location location) {
        for (Map.Entry<UUID, PendingInput> entry : pendingInputs.entrySet()) {

            PendingInput input = entry.getValue();
            if (input.matches(location) || input.matchesSupport(location)) {
                pendingInputs.remove(entry.getKey());
                return input;
            }
        }

        return null;
    }

    private void sendBlockChange(Player player, Vector3i pos, BlockData blockData) {
        int globalId = SpigotConversionUtil
                .fromBukkitBlockData(blockData)
                .getGlobalId();

        WrapperPlayServerBlockChange packet =
                new WrapperPlayServerBlockChange(pos, globalId);

        PacketEvents.getAPI()
                .getPlayerManager()
                .sendPacket(player, packet);
    }

    private void sendSignNbt(Player player, Vector3i pos, String[] lines) {

        NBTList<NBTString> messages =
                new NBTList<>(NBTType.STRING);

        NBTList<NBTString> filteredMessages =
                new NBTList<>(NBTType.STRING);

        for (String line : lines) {

            String text = line == null ? "" : line;

            Component component =
                    LegacyComponentSerializer.legacySection()
                            .deserialize(text);

            String json =
                    GsonComponentSerializer.gson()
                            .serialize(component);

            messages.addTag(new NBTString(json));
            filteredMessages.addTag(new NBTString(json));
        }

        NBTCompound frontText = new NBTCompound();

        frontText.setTag("messages", messages);
        frontText.setTag("filtered_messages", filteredMessages);
        frontText.setTag("color", new NBTString("black"));
        frontText.setTag("has_glowing_text", new NBTByte((byte) 0));

        NBTList<NBTString> backMessages =
                new NBTList<>(NBTType.STRING);

        NBTList<NBTString> backFilteredMessages =
                new NBTList<>(NBTType.STRING);

        for (int i = 0; i < 4; i++) {
            backMessages.addTag(new NBTString("{}"));
            backFilteredMessages.addTag(new NBTString("{}"));
        }

        NBTCompound backText = new NBTCompound();

        backText.setTag("messages", backMessages);
        backText.setTag("filtered_messages", backFilteredMessages);
        backText.setTag("color", new NBTString("black"));
        backText.setTag("has_glowing_text", new NBTByte((byte) 0));

        NBTCompound root = new NBTCompound();

        root.setTag("front_text", frontText);
        root.setTag("back_text", backText);
        root.setTag("is_waxed", new NBTByte((byte) 0));

        WrapperPlayServerBlockEntityData packet =
                new WrapperPlayServerBlockEntityData(
                        pos,
                        TileEntityType.SIGN,
                        root
                );

        PacketEvents.getAPI()
                .getPlayerManager()
                .sendPacket(player, packet);
    }

    private void sendOpenSignEditor(Player player, Vector3i pos) {
        WrapperPlayServerOpenSignEditor packet = new WrapperPlayServerOpenSignEditor(pos, true);

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    public record PendingInput(Consumer<String> callback, Location location, BlockData oldData, UUID playerId) {

        public boolean matches(Location other) {
            return sameBlock(other, 0);
        }

        public boolean matchesSupport(Location other) {
            return sameBlock(other, -1);
        }

        private boolean sameBlock(
                Location other,
                int yOffset
        ) {
            return other != null && Objects.equals(location.getWorld(), other.getWorld())
                    && location.getBlockX()
                    == other.getBlockX()
                    && location.getBlockY() + yOffset
                    == other.getBlockY()
                    && location.getBlockZ()
                    == other.getBlockZ();
        }

        public void restore() {

            Player player = org.bukkit.Bukkit.getPlayer(playerId);

            if (player != null && player.isOnline()) {

                Vector3i pos = SpigotConversionUtil.fromBukkitLocation(location).getPosition().toVector3i();

                int globalId = SpigotConversionUtil.fromBukkitBlockData(oldData).getGlobalId();

                WrapperPlayServerBlockChange packet = new WrapperPlayServerBlockChange(pos, globalId);

                PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
            }
        }
    }
}