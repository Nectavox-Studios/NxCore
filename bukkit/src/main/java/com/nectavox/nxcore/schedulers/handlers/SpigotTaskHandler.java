package com.nectavox.nxcore.schedulers.handlers;

import com.nectavox.nxcore.interfaces.TaskHandle;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class SpigotTaskHandler implements TaskHandle {

    private final BukkitTask task;

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
}
