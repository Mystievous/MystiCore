package com.starseekstudios.mysticore;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Namespaced;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;

public class SoundUtil {

    public static void errorNoise(Audience audience, Location location) {
        audience.playSound(Sound.sound(Key.key(Key.MINECRAFT_NAMESPACE, "block.note_block.bit"), Sound.Source.PLAYER, 1f, 0.529732f), location.x(), location.y(), location.z());
        audience.playSound(Sound.sound(Key.key(Key.MINECRAFT_NAMESPACE, "block.note_block.bit"), Sound.Source.PLAYER, 1f, 0.749154f), location.x(), location.y(), location.z());
        audience.playSound(Sound.sound(Key.key(Key.MINECRAFT_NAMESPACE, "block.note_block.bit"), Sound.Source.PLAYER, 1f, 1.059463f), location.x(), location.y(), location.z());
    }

    public static void successNoise(Audience audience, Location location) {
        audience.playSound(Sound.sound(NamespacedKey.minecraft("block.note_block.guitar"), Sound.Source.RECORD, 1f, 0.529732f), location.x(), location.y(), location.z());
        audience.playSound(Sound.sound(NamespacedKey.minecraft("block.note_block.guitar"), Sound.Source.RECORD, 1f, 1.059463f), location.x(), location.y(), location.z());
        audience.playSound(Sound.sound(NamespacedKey.minecraft("block.note_block.guitar"), Sound.Source.RECORD, 1f, 1.334840f), location.x(), location.y(), location.z());
        audience.playSound(Sound.sound(NamespacedKey.minecraft("block.note_block.guitar"), Sound.Source.RECORD, 1f, 1.587401f), location.x(), location.y(), location.z());
    }

}
