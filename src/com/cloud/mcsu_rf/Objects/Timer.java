package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Lambdas.TimerEventLambda;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class Timer {

    private int timeLeft;
    private final int tickIncrease;
    private final Timer thisTimer;
    private boolean stopwatchActive;
    private TimerEventLambda onTickIncrease = null;
    private TimerEventLambda onTimerEnd = null;

    public Timer(int tickIncrease, int length) {

        this.tickIncrease = tickIncrease; this.timeLeft = length;

        this.thisTimer = this;

        new BukkitRunnable() {
            public void run() {
                thisTimer.timeLeft += thisTimer.tickIncrease;
                if (thisTimer.onTickIncrease != null) { thisTimer.onTickIncrease.exec(thisTimer); }

                if(thisTimer.timeLeft <= 0) {
                    if (thisTimer.onTimerEnd != null) { thisTimer.onTimerEnd.exec(thisTimer); }
                    this.cancel();
                }
            }
        }.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);

    }

    public Timer(int tickIncrease) {

        this.tickIncrease = tickIncrease;

        this.thisTimer = this;

        new BukkitRunnable() {
            public void run() {
                thisTimer.timeLeft += thisTimer.tickIncrease;
                if (thisTimer.onTickIncrease != null) { thisTimer.onTickIncrease.exec(thisTimer); }

                if (!stopwatchActive) {
                    if (thisTimer.onTimerEnd != null) { thisTimer.onTimerEnd.exec(thisTimer); }
                    this.cancel();
                }

            }
        }.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);

    }

    public Timer setOnTickIncrease(TimerEventLambda onTickIncrease) { this.onTickIncrease = onTickIncrease; return this; }
    public Timer setOnTimerEnd(TimerEventLambda onTickIncrease) { this.onTimerEnd = onTickIncrease; return this; }

    public void disable() { this.stopwatchActive = false; }

    public int getTimeLeft() { return this.timeLeft; }

}
