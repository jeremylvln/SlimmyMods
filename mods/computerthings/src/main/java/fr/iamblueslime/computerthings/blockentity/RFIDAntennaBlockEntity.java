package fr.iamblueslime.computerthings.blockentity;

import fr.iamblueslime.computerthings.init.ModBlockEntities;
import fr.iamblueslime.computerthings.item.CardItem;
import fr.iamblueslime.computerthings.logic.computer.IArgumentHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class RFIDAntennaBlockEntity extends APeripheralBlockEntity {
    private static final double RANGE_SQUARED = Math.pow(10.0D, 2);

    public RFIDAntennaBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RFID_ANTENNA.get(), pos, state);

        this.setHasEventQueue();
        this.computerMethodRegistry.register("scan", this::onMethodScan);
    }

    @Override
    public String getPeripheralName() {
        return "rfid_antenna";
    }

    private Object[] onMethodScan(IArgumentHandle args) {
        for (Player player : this.level.players()) {
            if (EntitySelector.NO_SPECTATORS.test(player)) {
                double distanceSquared = this.worldPosition.distSqr(player.blockPosition());
                if (distanceSquared < RANGE_SQUARED)
                    RFIDAntennaBlockEntity.this.pushPlayerCards(player);
            }
        }

        return new Object[0];
    }

    private void pushPlayerCards(Player player) {
        player.getInventory().items.stream()
                .filter(itemStack -> !itemStack.isEmpty() && itemStack.getItem() instanceof CardItem && ((CardItem) itemStack.getItem()).type == CardItem.Type.RFID)
                .forEach((itemStack -> this.pushEvent(new Object[] { player.getName(), CardItem.getData(itemStack) })));
    }
}
