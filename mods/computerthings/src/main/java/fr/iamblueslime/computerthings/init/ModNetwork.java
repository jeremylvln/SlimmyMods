package fr.iamblueslime.computerthings.init;

import fr.iamblueslime.computerthings.ComputerThings;
import fr.iamblueslime.computerthings.network.ServerboundCraftElectronicPadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static SimpleChannel CHANNEL;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void init(FMLCommonSetupEvent event) {
        CHANNEL = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ComputerThings.MODID, "network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions((version) -> true)
                .serverAcceptedVersions((version) -> true)
                .simpleChannel();

        CHANNEL.messageBuilder(ServerboundCraftElectronicPadPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ServerboundCraftElectronicPadPacket::new)
                .encoder(ServerboundCraftElectronicPadPacket::toBytes)
                .consumer(ServerboundCraftElectronicPadPacket::handle)
                .add();
    }

    public static <Packet> void sendToServer(Packet message) {
        CHANNEL.sendToServer(message);
    }

    public static <Packet> void sendToPlayer(ServerPlayer player, Packet message) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
