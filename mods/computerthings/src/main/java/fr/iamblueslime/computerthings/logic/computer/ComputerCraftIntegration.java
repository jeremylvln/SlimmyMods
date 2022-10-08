package fr.iamblueslime.computerthings.logic.computer;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IDynamicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import fr.iamblueslime.computerthings.ComputerThings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

public class ComputerCraftIntegration {
    public static void load() {
        ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
    }

    public static class PeripheralProvider implements IPeripheralProvider {
        @NotNull
        @Override
        public LazyOptional<IPeripheral> getPeripheral(@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction direction) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof IComputerPeripheral)
                return LazyOptional.of(() -> new PeripheralHandle((IComputerPeripheral) blockEntity));
            return LazyOptional.empty();
        }
    }

    private static class PeripheralHandle implements IDynamicPeripheral {
        private final IComputerPeripheral computerPeripheral;

        public PeripheralHandle(IComputerPeripheral computerPeripheral) {
            this.computerPeripheral = computerPeripheral;
        }

        @NotNull
        @Override
        public String[] getMethodNames() {
            return this.computerPeripheral.getPeripheralMethods();
        }

        @NotNull
        @Override
        public MethodResult callMethod(@NotNull IComputerAccess computer, @NotNull ILuaContext context, int method, @NotNull IArguments arguments) throws LuaException {
            try {
                return MethodResult.of(this.computerPeripheral.invokePeripheralMethod(method, createArgumentHandle(arguments)));
            } catch (Exception e) {
                throw new LuaException(e.getMessage());
            }
        }

        @Nonnull
        @Override
        public String getType() {
            return this.computerPeripheral.getPeripheralName();
        }

        @Nonnull
        @Override
        public Set<String> getAdditionalTypes() {
            return Collections.singleton(ComputerThings.MODID);
        }

        @Nullable
        public Object getTarget() {
            return this.computerPeripheral;
        }

        @Override
        public boolean equals(@Nullable IPeripheral other) {
            return other instanceof PeripheralHandle
                    && ((PeripheralHandle) other).computerPeripheral.equals(this.computerPeripheral);
        }

        private static IArgumentHandle createArgumentHandle(IArguments arguments) {
            return new IArgumentHandle() {
                @Override
                public int count() {
                    return arguments.count();
                }

                @Override
                public Object[] getAll() {
                    return arguments.getAll();
                }

                @Override
                public Object get(int index) {
                    return arguments.get(index);
                }

                @Override
                public double getDouble(int index) throws Exception {
                    return arguments.getDouble(index);
                }

                @Override
                public int getInt(int index) throws Exception {
                    return arguments.getInt(index);
                }

                @Override
                public long getLong(int index) throws Exception {
                    return arguments.getLong(index);
                }

                @Override
                public boolean getBoolean(int index) throws Exception {
                    return arguments.getBoolean(index);
                }

                @Override
                public String getString(int index) throws Exception {
                    return arguments.getString(index);
                }
            };
        }
    }
}
