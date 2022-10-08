package fr.iamblueslime.computerthings.blockentity;

import fr.iamblueslime.computerthings.logic.computer.ComputerEvent;
import fr.iamblueslime.computerthings.logic.computer.ComputerMethodRegistry;
import fr.iamblueslime.computerthings.logic.computer.IArgumentHandle;
import fr.iamblueslime.computerthings.logic.computer.IComputerPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class APeripheralBlockEntity extends BlockEntity implements IComputerPeripheral {
    protected final ComputerMethodRegistry computerMethodRegistry = new ComputerMethodRegistry();
    private final Queue<ComputerEvent> queuedEvents = new ConcurrentLinkedQueue<>();

    public APeripheralBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected void tickCommandQueue() {
        for (ComputerEvent computerEvent : this.queuedEvents)
            computerEvent.decrTTL();

        this.queuedEvents.removeIf((computerEvent) -> computerEvent.getTTL() < 0);
    }

    protected void pushEvent(Object[] payload) {
        this.queuedEvents.add(new ComputerEvent(payload, 20));
    }

    protected void setHasEventQueue() {
        this.computerMethodRegistry.register("pollEvent", this::onMethodPollEvent);
        this.computerMethodRegistry.register("getEventCount", this::onMethodGetEventCount);
        this.computerMethodRegistry.register("hasEvent", this::onMethodHasEvent);
    }

    @Override
    public Object[] invokePeripheralMethod(int index, IArgumentHandle args) throws Exception {
        return this.computerMethodRegistry.invokeMethod(index, args);
    }

    @Override
    public String[] getPeripheralMethods() {
        return this.computerMethodRegistry.getMethods();
    }

    private Object[] onMethodPollEvent(IArgumentHandle args) {
        if (this.queuedEvents.isEmpty())
            return new Object[0];

        return this.queuedEvents.poll().getPayload();
    }

    private Object[] onMethodGetEventCount(IArgumentHandle args) {
        return new Object[] { this.queuedEvents.size() };
    }

    private Object[] onMethodHasEvent(IArgumentHandle args) {
        return new Object[] { !this.queuedEvents.isEmpty() };
    }
}
