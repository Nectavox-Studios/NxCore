package com.nectavox.nxcore.schedulers;

import com.nectavox.nxcore.interfaces.SchedulerAdapter;
import com.nectavox.nxcore.interfaces.TaskHandle;
import com.nectavox.nxcore.schedulers.handlers.PaperTaskHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class PaperScheduler implements SchedulerAdapter {
    private final JavaPlugin plugin;

    @Override
    public TaskHandle execute(Runnable task) {
        return new PaperTaskHandler(Bukkit.getGlobalRegionScheduler().run(plugin, t -> task.run()));
    }

    @Override
    public TaskHandle executeAsync(Runnable task) {
        return new PaperTaskHandler(Bukkit.getAsyncScheduler().runNow(plugin, t -> task.run()));
    }

    @Override
    public TaskHandle runLater(Runnable task, long delayTicks) {
        return new PaperTaskHandler(Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> task.run(), delayTicks));
    }

    @Override
    public TaskHandle runLaterAsync(Runnable task, long delayTicks) {
        return new PaperTaskHandler(Bukkit.getAsyncScheduler().runDelayed(plugin, t -> task.run(), ticksToMillis(delayTicks), TimeUnit.MILLISECONDS));
    }

    @Override
    public TaskHandle runTimer(Consumer<TaskHandle> consumer, long delayTicks, long periodTicks) {
        final TaskHandle[] handle = new TaskHandle[1];

        handle[0] = new PaperTaskHandler(
                Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                        plugin,
                        task -> consumer.accept(handle[0]),
                        delayTicks,
                        periodTicks
                )
        );

        return handle[0];
    }

    @Override
    public TaskHandle runTimerAsync(Consumer<TaskHandle> consumer, long delayTicks, long periodTicks) {
        final TaskHandle[] handle = new TaskHandle[1];

        handle[0] = new PaperTaskHandler(
                Bukkit.getAsyncScheduler().runAtFixedRate(
                        plugin,
                        task -> consumer.accept(handle[0]),
                        ticksToMillis(delayTicks),
                        ticksToMillis(periodTicks),
                        TimeUnit.MILLISECONDS
                )
        );

        return handle[0];
    }


    @Override
    public @Nullable TaskHandle executeForEntity(Runnable task, @NotNull Object entity) {
        if (entity instanceof Entity bukkitEntity) {
            return new PaperTaskHandler(bukkitEntity.getScheduler().run(plugin, t -> task.run(), null));
        }
        return null;
    }

    @Override
    public @Nullable TaskHandle runLaterForEntity(Runnable task, long delayTicks, @NotNull Object entity) {
        if (entity instanceof Entity bukkitEntity) {
            return new PaperTaskHandler(bukkitEntity.getScheduler().runDelayed(plugin, t -> task.run(), null, delayTicks));
        }
        return null;
    }

    @Override
    public @Nullable TaskHandle runTimerForEntity(
            Consumer<TaskHandle> consumer,
            long delayTicks,
            long periodTicks,
            @NotNull Object entity
    ) {
        if (entity instanceof Entity bukkitEntity) {
            final TaskHandle[] handle = new TaskHandle[1];

            handle[0] = new PaperTaskHandler(
                    bukkitEntity.getScheduler().runAtFixedRate(
                            plugin,
                            task -> consumer.accept(handle[0]),
                            null,
                            delayTicks,
                            periodTicks
                    )
            );

            return handle[0];
        }

        return null;
    }

    private long ticksToMillis(long ticks) {
        return ticks * 50L;
    }
}
