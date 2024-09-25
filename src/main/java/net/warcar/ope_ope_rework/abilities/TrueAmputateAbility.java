package net.warcar.ope_ope_rework.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.abilities.ope.RoomAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.ope.SpatialSlashProjectile;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

public class TrueAmputateAbility extends Ability {
    public static final AbilityCore<TrueAmputateAbility> INSTANCE = new AbilityCore.Builder<>("Amputate", AbilityCategory.DEVIL_FRUITS, TrueAmputateAbility::new).addDescriptionLine("The user cuts the every block on his sight, horizontal sneaking causes a vertical cut").build();

    private final ProjectileComponent projectileComponent;

    public TrueAmputateAbility(AbilityCore<TrueAmputateAbility> core) {
        super(core);
        projectileComponent = new ProjectileComponent(this, entity -> new SpatialSlashProjectile(entity.level, entity));
        this.addComponents(projectileComponent);
        this.isNew = true;
        this.addUseEvent(this::onUse);
    }

    private void onUse(LivingEntity entity, IAbility ability) {
        RoomAbility roomAbility = AbilityDataCapability.get(entity).getEquippedAbility(RoomAbility.INSTANCE);
        if (DevilFruitCapability.get(entity).hasAwakenedFruit() || (roomAbility != null && roomAbility.isPositionInRoom(entity.blockPosition()))) {
            for (int i = -15; i < 16; i++) {
                this.projectileComponent.shoot(entity);
                if (entity.isSteppingCarefully()) {
                    this.projectileComponent.getShotProjectile().shootFromRotation(entity.xRot + i, entity.yRot, 0, 2, 0);
                } else {
                    this.projectileComponent.getShotProjectile().shootFromRotation(entity.xRot, entity.yRot + i , 0, 2, 0);
                }
            }
            this.cooldownComponent.startCooldown(entity, 200);
        } else {
            entity.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_ONLY_IN_ROOM, this.getDisplayName()), Util.NIL_UUID);
        }
    }
}
