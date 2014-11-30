package me.jezza.oc.common.core;

import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * Still unrefined.
 * Don't use it yet.
 * TODO Fix this, make it a static thing. Such as an ENUM, or Factory.
 */
public class Sound {
    private String name;

    public Sound(String name) {
        String modIdentifier = Loader.instance().activeModContainer().getModId() + ":";
        this.name = modIdentifier + name;
    }

    public String getName() {
        return name;
    }

    public void play(Entity entity) {
        play(entity, 1.0F, 1.0F);
    }

    public void play(Entity entity, float volume, float pitch) {
        entity.worldObj.playSoundAtEntity(entity, name, volume, pitch);
    }

    public void play(World world, double x, double y, double z, float volume, float pitch) {
        world.playSound(x, y, z, name, volume, pitch, false);
    }

    public void play(World world, double x, double y, double z) {
        play(world, x, y, z, 1.0F, 1.0F);
    }

    public void play(World world, int x, int y, int z, float volume, float pitch) {
        play(world, x + 0.5F, y + 0.5F, z + 0.5F, volume, pitch);
    }

    public void play(World world, int x, int y, int z) {
        play(world, x + 0.5F, y + 0.5F, z + 0.5F, 1.0F, 1.0F);
    }

}