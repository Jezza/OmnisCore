package me.jezza.oc.common.utils;

public class TimeTickerCollection {

    private TimeTicker[] timeTickers;
    private int size;

    public TimeTickerCollection(int count, int startingAmount, int upper, int lower) {
        size = count;
        timeTickers = new TimeTicker[count];
        for (int i = 0; i < count; i++)
            timeTickers[i] = new TimeTicker(startingAmount, upper, lower);
    }

    public TimeTickerCollection(int count, int startingAmount, int upper) {
        this(count, startingAmount, upper, 0);
    }

    public TimeTickerCollection(int count, int upper) {
        this(count, 0, upper, 0);
    }

    public boolean tick(int index) {
        if (!validIndex(index))
            return false;
        return timeTickers[index].tick();
    }

    public boolean[] tickAll() {
        boolean[] tickedArray = new boolean[size];
        for (int i = 0; i < size; i++)
            tickedArray[i] = timeTickers[i].tick();
        return tickedArray;
    }

    private boolean validIndex(int index) {
        return index >= 0 && index < timeTickers.length;
    }

}
