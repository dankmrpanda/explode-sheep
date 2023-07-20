package org.cornchips.explode_sheep.Mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.TntEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntExplodePowerMixin extends Entity implements Ownable {
    public TntExplodePowerMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "explode()V", at = @At("HEAD"), cancellable = true)
    private void explode(CallbackInfo ci) {
        float f = 20.0F;
        this.getWorld().createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), f, World.ExplosionSourceType.TNT);
        ci.cancel();
    }
}