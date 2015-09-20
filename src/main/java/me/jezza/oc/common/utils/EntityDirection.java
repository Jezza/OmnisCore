package me.jezza.oc.common.utils;

/**
 * This class is fairly pointless.
 *
 * @author Jezza
 */
@Deprecated
public enum EntityDirection {

    UPPER_NORTH_WEST(-1, 1, -1), UPPER_NORTH(0, 1, -1), UPPER_NORTH_EAST(1, 1, -1),
    UPPER_WEST(-1, 1, 0),       UP(0, 1, 0), UPPER_EAST(1, 1, 0),
    UPPER_SOUTH_WEST(-1, 1, 1), UPPER_SOUTH(0, 1, 1), UPPER_SOUTH_EAST(1, 1, 1),

    NORTH_WEST(-1, 0, -1), NORTH(0, 0, -1), NORTH_EAST(1, 0, -1),
    WEST(-1, 0, 0),       UNKNOWN(0, 0, 0), EAST(1, 0, 0),
    SOUTH_WEST(-1, 0, 1), SOUTH(0, 0, 1), SOUTH_EAST(1, 0, 1),

    LOWER_NORTH_WEST(-1, -1, -1), LOWER_NORTH(0, -1, -1), LOWER_NORTH_EAST(1, -1, -1),
    LOWER_WEST(-1, -1, 0),       DOWN(0, -1, 0),        LOWER_EAST(1, -1, 0),
    LOWER_SOUTH_WEST(-1, -1, 1), LOWER_SOUTH(0, -1, 1), LOWER_SOUTH_EAST(1, -1, 1);

    public static EntityDirection[] DIRECT_OPPOSITES = {
            LOWER_SOUTH_EAST, LOWER_SOUTH, LOWER_SOUTH_WEST, LOWER_EAST,   DOWN,       LOWER_WEST,  LOWER_NORTH_EAST, LOWER_NORTH, LOWER_NORTH_WEST,
                  SOUTH_EAST,       SOUTH,       SOUTH_WEST,       EAST, UNKNOWN,             WEST,        NORTH_EAST,       NORTH,       NORTH_WEST,
            UPPER_SOUTH_EAST, UPPER_SOUTH, UPPER_SOUTH_WEST, UPPER_EAST,     UP,       UPPER_WEST,  UPPER_NORTH_EAST, UPPER_NORTH, UPPER_NORTH_WEST
    };

    public static EntityDirection[] FLATTEN = {
            NORTH_WEST, NORTH, NORTH_EAST, WEST, UNKNOWN, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST,
            NORTH_WEST, NORTH, NORTH_EAST, WEST, UNKNOWN, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST,
            NORTH_WEST, NORTH, NORTH_EAST, WEST, UNKNOWN, EAST, SOUTH_WEST, SOUTH, SOUTH_EAST
    };

    public final int xOffset, yOffset, zOffset;

    EntityDirection(int xOffset, int yOffset, int zOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public EntityDirection directOpposite() {
        return DIRECT_OPPOSITES[ordinal()];
    }

    public EntityDirection flatten() {
        return FLATTEN[ordinal()];
    }
}
