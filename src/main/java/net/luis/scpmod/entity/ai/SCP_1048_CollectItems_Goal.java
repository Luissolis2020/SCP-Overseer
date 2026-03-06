package net.luis.scpmod.entity.ai;

import net.luis.scpmod.entity.custom.SCP_1048_Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class SCP_1048_CollectItems_Goal extends Goal {
    private final SCP_1048_Entity bear;
    private ItemEntity targetItem;

    public SCP_1048_CollectItems_Goal(SCP_1048_Entity bear) {
        this.bear = bear;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (bear.getBuildAnimationTicks() > 0) return false;

        List<ItemEntity> items = bear.level().getEntitiesOfClass(
                ItemEntity.class,
                bear.getBoundingBox().inflate(5.0),
                item -> item.isAlive() && !item.getItem().isEmpty()
        );

        targetItem = items.stream()
                .min(Comparator.comparingDouble(item -> item.distanceToSqr(bear)))
                .orElse(null);

        return targetItem != null;
    }

    @Override
    public void start() {
        if (targetItem != null) {
            bear.getNavigation().moveTo(targetItem, 1.0);
        }
    }

    @Override
    public void tick() {
        if (targetItem != null && targetItem.isAlive()) {
            bear.getLookControl().setLookAt(targetItem);
            if (bear.distanceToSqr(targetItem) < 2.0) {
                if (bear.addItemToInventory(targetItem.getItem())) {
                    targetItem.discard();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return targetItem != null && targetItem.isAlive() && bear.distanceToSqr(targetItem) > 2.0;
    }
}
