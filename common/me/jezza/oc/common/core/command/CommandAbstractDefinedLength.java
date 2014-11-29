package me.jezza.oc.common.core.command;

import me.jezza.oc.common.utils.CoordSet;
import net.minecraft.command.ICommandSender;

public abstract class CommandAbstractDefinedLength extends CommandAbstract {

    int minimumLength = -1;
    int maximumLength = -1;

    public CommandAbstractDefinedLength(String commandName, String commandUsage) {
        super(commandName, commandUsage);
    }

    /**
     * @param minimumLength Set to -1 to ignore.
     * @param maximumLength Set to -1 to ignore.
     */
    public CommandAbstractDefinedLength(String commandName, String commandUsage, int minimumLength, int maximumLength) {
        super(commandName, commandUsage);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (minimumLength >= 0)
            if (args.length < minimumLength) {
                sendCommandUsage(commandSender);
                return;
            }
        if (minimumLength >= 0)
            if (args.length > maximumLength) {
                sendCommandUsage(commandSender);
                return;
            }

        processValidCommand(commandSender, args);
    }

    public abstract void processValidCommand(ICommandSender commandSender, String[] args);

    /**
     * After this:
     * - firstSet will contain the lowest values.
     * - secondSet will contain the highest values.
     */
    public void sortCoordSets(CoordSet firstSet, CoordSet secondSet) {
        if (secondSet.getX() < firstSet.getX())
            swapCoordSetOnAxis(firstSet, secondSet, Axis.X);

        if (secondSet.getY() < firstSet.getY())
            swapCoordSetOnAxis(firstSet, secondSet, Axis.Y);

        if (secondSet.getZ() < firstSet.getZ())
            swapCoordSetOnAxis(firstSet, secondSet, Axis.Z);
    }

    public void swapCoordSetOnAxis(CoordSet firstSet, CoordSet secondSet, Axis axis) {
        int temp;
        switch (axis) {
            case X:
                temp = firstSet.getX();
                firstSet.setX(secondSet.getX());
                secondSet.setX(temp);
                break;
            case Y:
                temp = firstSet.getY();
                firstSet.setY(secondSet.getY());
                secondSet.setY(temp);
                break;
            case Z:
                temp = firstSet.getZ();
                firstSet.setZ(secondSet.getZ());
                secondSet.setZ(temp);
                break;
        }
    }

    public enum Axis {
        X, Y, Z;
    }
}
