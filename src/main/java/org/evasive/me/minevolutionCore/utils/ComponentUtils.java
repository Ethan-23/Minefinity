package org.evasive.me.minevolutionCore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ComponentUtils {

    public static Component makeText(String text, TextColor color, Boolean bold){
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, bold);
    }

    public static Component makeText(String text, NamedTextColor color, Boolean bold){
        return Component.text(text, color).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, bold);
    }

    public static Component makeText(String text, int hexCode, Boolean bold){
        return Component.text(text, TextColor.color(hexCode)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, bold);
    }

}
