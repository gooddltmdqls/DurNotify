package kr.icetang0123.durnotify.client.mixins;

import kr.icetang0123.durnotify.client.DurNotifyClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Timer;
import java.util.TimerTask;

@Mixin(PlayerManager.class)
@Environment(EnvType.CLIENT)
public class PlayerJoinMixin {
    @Inject(at = @At("HEAD"), method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        String latestRelease = DurNotifyClient.LATEST_RELEASE;
        String latestBeta = DurNotifyClient.LATEST_BETA;
        String latestAlpha = DurNotifyClient.LATEST_ALPHA;

        String currentVer = DurNotifyClient.CURRENT_VERSION;

        if ((latestRelease != null && !latestRelease.equals(currentVer)) && (latestBeta != null && !latestBeta.equals(currentVer)) && (latestAlpha != null && !latestAlpha.equals(currentVer))) {
            Timer timer = new Timer();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.sendMessage(Text.translatable("title.durnotify.new_ver", Text.of("https://modrinth.com/project/dur-notify").copy().setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://modrinth.com/project/dur-notify")).withUnderline(true))));
                }
            }, 5000);
        }
    }
}
