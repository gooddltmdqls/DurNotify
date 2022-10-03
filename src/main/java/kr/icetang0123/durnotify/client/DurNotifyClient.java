package kr.icetang0123.durnotify.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtType;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class DurNotifyClient implements ClientModInitializer {
    public Logger LOGGER = LoggerFactory.getLogger(DurNotifyClient.class);
    @Override
    public void onInitializeClient() {
        LOGGER.info("DurNotify have been initialized!");

        ClientTickEvents.END_CLIENT_TICK.register(e -> {
            if (e.world == null) return;

            assert e.player != null;
            ItemStack item = e.player.getInventory().getMainHandStack();
            ItemStack arm1 = e.player.getInventory().armor.get(0);
            ItemStack arm2 = e.player.getInventory().armor.get(1);
            ItemStack arm3 = e.player.getInventory().armor.get(2);
            ItemStack arm4 = e.player.getInventory().armor.get(3);

            NbtCompound nbt = item.getNbt();
            NbtCompound nbt1 = arm1.getNbt();
            NbtCompound nbt2 = arm2.getNbt();
            NbtCompound nbt3 = arm3.getNbt();
            NbtCompound nbt4 = arm4.getNbt();

            if (nbt == null || nbt1 == null || nbt2 == null || nbt3 == null || nbt4 == null) return;

            if (item.isDamageable() && (item.getItem().getMaxDamage() - item.getDamage()) <= 50 && !nbt.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn").formatted(Formatting.RED), true);

                nbt.putBoolean("notified", true);

                item.setNbt(nbt);
            } else if (item.isDamageable() && (item.getItem().getMaxDamage() - item.getDamage()) > 50) {
                nbt.putBoolean("notified", false);

                item.setNbt(nbt);
            }

            if (arm1.isDamageable() && (arm1.getItem().getMaxDamage() - arm1.getDamage()) <= 50 && !nbt1.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt1.putBoolean("notified", true);

                arm1.setNbt(nbt1);
            } else if (arm1.isDamageable() && (arm1.getItem().getMaxDamage() - arm1.getDamage()) > 50) {
                nbt1.putBoolean("notified", false);

                arm1.setNbt(nbt1);
            }

            if (arm2.isDamageable() && (arm2.getItem().getMaxDamage() - arm2.getDamage()) <= 50 && !nbt2.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt2.putBoolean("notified", true);

                arm2.setNbt(nbt1);
            } else if (arm2.isDamageable() && (arm2.getItem().getMaxDamage() - arm2.getDamage()) > 50) {
                nbt2.putBoolean("notified", false);

                arm2.setNbt(nbt2);
            }

            if (arm3.isDamageable() && (arm3.getItem().getMaxDamage() - arm3.getDamage()) <= 50 && !nbt3.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt3.putBoolean("notified", true);

                arm3.setNbt(nbt3);
            } else if (arm3.isDamageable() && (arm3.getItem().getMaxDamage() - arm3.getDamage()) > 50) {
                nbt3.putBoolean("notified", false);

                arm3.setNbt(nbt3);
            }

            if (arm4.isDamageable() && (arm4.getItem().getMaxDamage() - arm4.getDamage()) <= 50 && !nbt4.getBoolean("notified")) {
                e.player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 2);

                e.player.sendMessage(Text.translatable("title.durnotify.warn_armor").formatted(Formatting.RED), true);

                nbt4.putBoolean("notified", true);

                arm4.setNbt(nbt1);
            } else if (arm4.isDamageable() && (arm4.getItem().getMaxDamage() - arm4.getDamage()) > 50) {
                nbt4.putBoolean("notified", false);

                arm4.setNbt(nbt4);
            }
        });
    }
}