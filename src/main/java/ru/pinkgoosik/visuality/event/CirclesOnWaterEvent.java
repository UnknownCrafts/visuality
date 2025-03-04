package ru.pinkgoosik.visuality.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import ru.pinkgoosik.visuality.VisualityMod;
import ru.pinkgoosik.visuality.registry.VisualityParticles;
import ru.pinkgoosik.visuality.util.ParticleUtils;

import java.util.Random;

public class CirclesOnWaterEvent {
    static final Random random = new Random();

    public static void onTick(ClientLevel world) {
        if (!VisualityMod.CONFIG.getBoolean("enabled", "water_circles")) return;
        if(Minecraft.getInstance().isPaused()) return;
        if (Minecraft.getInstance().options.particles == ParticleStatus.MINIMAL) return;
        AbstractClientPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        if(player.isUnderWater() || !Level.OVERWORLD.equals(world.dimension())) return;
        if(!world.isRaining()) return;
        Biome biome = world.getBiome(player.getOnPos()).value();
        if (!(biome.getPrecipitation().equals(Biome.Precipitation.RAIN)) || !(biome.warmEnoughToRain(player.getOnPos()))) return;

        for (int i = 0; i<= random.nextInt(10) + 5; i++) {
            int x = random.nextInt(15) - 7;
            int z = random.nextInt(15) - 7;
            BlockPos playerPos = new BlockPos((int)player.getX() + x, (int)player.getY(), (int)player.getZ() + z);
            BlockPos pos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, playerPos);

            if (world.getBlockState(pos.below()).is(Blocks.WATER) && world.getBlockState(pos).isAir()){
                if(world.getFluidState(pos.below()).getAmount() == 8) {

                    int color;
                    if(VisualityMod.CONFIG.getBoolean("colored", "water_circles")) {
                        color = biome.getWaterColor();
                    }
                    else color = 0;

                    ParticleUtils.add(world, VisualityParticles.WATER_CIRCLE, pos.getX() + random.nextDouble(), pos.getY() + 0.05D, pos.getZ()  + random.nextDouble(), color);
                }
            }
        }
    }
}
