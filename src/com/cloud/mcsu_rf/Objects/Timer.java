package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.Lambdas.TimerEventLambda;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {

    private int timeLeft;
    private int tickIncrease;
    private TimerEventLambda onTickIncrease = null;
    private TimerEventLambda onTimerEnd = null;

    public Timer(int tickIncrease, int length) { this.tickIncrease = tickIncrease; this.timeLeft = length; }

    public Timer setOnTickIncrease(TimerEventLambda onTickIncrease) { this.onTickIncrease = onTickIncrease; return this; }
    public Timer setOnTimerEnd(TimerEventLambda onTickIncrease) { this.onTimerEnd = onTickIncrease; return this; }

    public int getTimeLeft() { return this.timeLeft; }

    @Override
    public void run() {

        timeLeft += tickIncrease;
        this.onTickIncrease.exec(this);
        Bukkit.getLogger().info("ran ontick");

        if(timeLeft <= 0) {
            this.onTimerEnd.exec(this);

            cancel();
        }

    }
    
}
