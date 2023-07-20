package org.cornchips.explode_sheep.Mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.HealthBoostStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.potion.PotionUtil;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.Shearable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SheepEntity.class)
public abstract class ExplosiveSheepMixin extends AnimalEntity implements Shearable {
    @Shadow public abstract DyeColor getColor();

    protected ExplosiveSheepMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS)) {
            if (!this.getWorld().isClient && this.isShearable()) {
                this.sheared(SoundCategory.PLAYERS);
                this.emitGameEvent(GameEvent.SHEAR, player);
                itemStack.damage(1, player, (playerx) -> {
                    playerx.sendToolBreakStatus(hand);
                });
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 255));
                for (int i = 0; i < this.getY() + 64; i += 5) {
                    TntEntity tntEntity = new TntEntity(this.getWorld(), this.getX(), this.getY()-i, this.getZ(), null);
                    tntEntity.setFuse(80);
                    this.getWorld().spawnEntity(tntEntity);
                }//summon boom thing
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.CONSUME;
            }
        } else {
            return super.interactMob(player, hand);
        }
    }
}
