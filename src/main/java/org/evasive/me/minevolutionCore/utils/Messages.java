package org.evasive.me.minevolutionCore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class Messages {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    public static Component parse(String mm) {
        return MM.deserialize(mm).decoration(TextDecoration.ITALIC, false);
    }

    public static Component parse(String mm, TagResolver... tags) {
        return MM.deserialize(mm, tags).decoration(TextDecoration.ITALIC, false);
    }
}
