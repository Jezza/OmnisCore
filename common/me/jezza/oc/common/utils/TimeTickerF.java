package me.jezza.oc.common.utils;

import net.minecraft.util.MathHelper;

public class TimeTickerF {

    private float amount, upper, lower, stepAmount;

    public TimeTickerF(float startingAmount, float upper, float lower) {
        amount = MathHelper.clamp_float(startingAmount, lower, upper);
        this.upper = upper;
        this.lower = lower;
        stepAmount = 1.0F;
    }

    public TimeTickerF(float startingAmount, float upper) {
        this(startingAmount, upper, 0.0F);
    }

    public TimeTickerF setStepAmount(float stepAmount) {
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

    public float getAmount() {
        return amount;
    }

}
