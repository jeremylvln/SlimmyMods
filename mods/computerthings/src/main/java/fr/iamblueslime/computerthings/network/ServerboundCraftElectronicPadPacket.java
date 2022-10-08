package fr.iamblueslime.computerthings.network;

import fr.iamblueslime.computerthings.menu.ElectronicPadDesignerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundCraftElectronicPadPacket {
    public ServerboundCraftElectronicPadPacket() {
    }

    public ServerboundCraftElectronicPadPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player.containerMenu instanceof ElectronicPadDesignerMenu) {
                ((ElectronicPadDesignerMenu) player.containerMenu).craftPad();
            }
        });

        return true;
    }
}
