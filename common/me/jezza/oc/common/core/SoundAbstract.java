package me.jezza.oc.common.core;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public abstract class SoundAbstract {
    private String name;

    public SoundAbstract(String name) {
        this.name = getModIdentifier() + name;
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

    public void playAtPlayer(EntityLivingBase player) {
        play(player.worldObj, player.posX, player.posY, player.posZ);
    }

    public void playAtPlayer(EntityLivingBase player, float volume, float pitch) {
        play(player.worldObj, player.posX, player.posY, player.posZ, volume, pitch);
    }

    public abstract String getModIdentifier();

}
