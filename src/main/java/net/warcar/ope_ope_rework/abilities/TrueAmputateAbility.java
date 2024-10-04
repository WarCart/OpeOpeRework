package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.ope_ope_rework.projectiles.RealSlashProjectile;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.ope.SpatialSlashProjectile;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

public class TrueAmputateAbility extends Ability {
    public static final AbilityCore<TrueAmputateAbility> INSTANCE = new AbilityCore.Builder<>("Amputate", AbilityCategory.DEVIL_FRUITS, TrueAmputateAbility::new).addDescriptionLine("The user cuts the every block on his sight, horizontal sneaking causes a vertical cut").build();

    public TrueAmputateAbility(AbilityCore<TrueAmputateAbility> core) {
        super(core);
        this.onUseEvent = this::onUse;
        this.setMaxCooldown(6);
    }

    private boolean onUse(PlayerEntity entity) {
        RoomAbility roomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        if (AwakeningDataCapability.get(entity).isAwakened() || (roomAbility != null && roomAbility.isPositionInRoom(entity.blockPosition()))) {
            for (int i = -15; i < 16; i++) {
                RealSlashProjectile projectile = new RealSlashProjectile(entity.level, entity);
                if (entity.isSteppingCarefully()) {
                    projectile.shootFromRotation(entity.xRot + i, entity.yRot, 0, 2, 0);
                } else {
                    projectile.shootFromRotation(entity.xRot, entity.yRot + i , 0, 2, 0);
                }
                entity.level.addFreshEntity(projectile);
            }
            return true;
        } else {
            entity.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_ONLY_IN_ROOM, this.getDisplayName()), Util.NIL_UUID);
            return false;
        }
    }
}
