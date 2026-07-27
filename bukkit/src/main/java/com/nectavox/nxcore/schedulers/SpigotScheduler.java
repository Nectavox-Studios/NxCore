package com.nectavox.nxcore.schedulers;

import com.nectavox.nxcore.interfaces.SchedulerAdapter;
import com.nectavox.nxcore.interfaces.TaskHandle;
import com.nectavox.nxcore.schedulers.handlers.SpigotTaskHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class SpigotScheduler implements SchedulerAdapter {

    private final JavaPlugin plugin;

    @Override
    public TaskHandle execute(Runnable task) {
        return new SpigotTaskHandler(
                Bukkit.getScheduler().runTask(plugin, task)
        );
    }

    @Override
    public TaskHandle executeAsync(Runnable task) {
        return new SpigotTaskHandler(
                Bukkit.getScheduler().runTaskAsynchronously(plugin, task)
        );
    }

    @Override
    public TaskHandle runLater(Runnable task, long delayTicks) {
        return new SpigotTaskHandler(
                Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks)
        );
    }

    @Override
    public TaskHandle runLaterAsync(Runnable task, long delayTicks) {
        return new SpigotTaskHandler(
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks)
        );
    }

    @Override
    public TaskHandle runTimer(Consumer<TaskHandle> consumer, long delayTicks, long periodTicks) {
        final TaskHandle[] handle = new TaskHandle[1];

        handle[0] = new SpigotTaskHandler(
                Bukkit.getScheduler().runTaskTimer(
                        plugin,
                        () -> consumer.accept(handle[0]),
                        delayTicks,
                        periodTicks
                )
        );

        return handle[0];
    }

    @Override
    public TaskHandle runTimerAsync(Consumer<TaskHandle> consumer, long delayTicks, long periodTicks) {
        final TaskHandle[] handle = new TaskHandle[1];

        handle[0] = new SpigotTaskHandler(
                Bukkit.getScheduler().runTaskTimerAsynchronously(
                        plugin,
                        () -> consumer.accept(handle[0]),
                        delayTicks,
                        periodTicks
                )
        );

        return handle[0];
    }

    @Override
    public TaskHandle executeForEntity(Runnable task, @Nullable Object entity) {
        return execute(task);
    }

    @Override
    public TaskHandle runLaterForEntity(Runnable task, long delayTicks, @Nullable Object entity) {
        return runLater(task, delayTicks);
    }

    @Override
    public TaskHandle runTimerForEntity(Consumer<TaskHandle> task, long delayTicks, long periodTicks, @Nullable Object entity) {
        return runTimer(task, delayTicks, periodTicks);
    }
}