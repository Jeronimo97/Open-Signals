package com.troblecodings.signals.mixin;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.troblecodings.signals.blocks.GhostBlock;
import com.troblecodings.signals.blocks.Signal;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Redirect(
            method = "lambda$loadModel$22(Ljava/util/Map;Lnet/minecraft/resources/ResourceLocation;"
                    + "Lcom/mojang/datafixers/util/Pair;Ljava/util/Map;"
                    + "Lnet/minecraft/client/resources/model/ModelResourceLocation;"
                    + "Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At(value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V",
                    remap = false))
    private void opensignals$suppressSignalBlockstateWarning(final Logger logger,
            final String format, final Object blockstatePath, final Object modelLocation) {

        if (modelLocation instanceof ModelResourceLocation loc) {

            ResourceLocation blockId = new ResourceLocation(loc.getNamespace(), loc.getPath());

            Block block = ForgeRegistries.BLOCKS.getValue(blockId);

            if (block instanceof Signal || block instanceof GhostBlock)
                return;
        }

        logger.warn(format, blockstatePath, modelLocation);
    }

}
