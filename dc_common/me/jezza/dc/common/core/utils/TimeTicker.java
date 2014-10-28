package me.jezza.dc.common.core.utils;

import net.minecraft.util.MathHelper;

public class TimeTicker {

    private int amount, upper, lower, stepAmount;

    public TimeTicker(int startingAmount, int upper, int lower) {
        amount = MathHelper.clamp_int(startingAmount, lower, upper);
        this.upper = upper;
        this.lower = lower;
        stepAmount = 1;
    }

    public TimeTicker(int startingAmount, int upper) {
        this(startingAmount, upper, 0);
    }

    public TimeTicker setStepAmount(int stepAmount) {
        this.stepAmount = stepAmount;
        return this;
    }

    public void reset() {
        amount = lower;
    }

    public boolean tick() {
        amount += stepAmount;
        if (amount >= upper)
            amount = lower;
        return amount == lower;
    }

    public int getAmount() {
        return amount;
    }

}
