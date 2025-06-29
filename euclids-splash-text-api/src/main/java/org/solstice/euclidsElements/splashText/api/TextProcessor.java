package org.solstice.euclidsElements.splashText.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class TextProcessor {

    protected static final Map<String, Function<MinecraftClient, String>> FORMATS = new HashMap<>();

	public static void addFormat(String format, Function<MinecraftClient, String> formatter) {
		FORMATS.put(format, formatter);
	}

	static {
		addFormat("%USERNAME%", client -> client.getGameProfile().getName().toUpperCase());
		addFormat("%username%", client -> client.getGameProfile().getName());
	}

    public static String format(String string) {
        if (!string.contains("%")) return string;

        for (String find : FORMATS.keySet()) {
            String replace = FORMATS.get(find).apply(MinecraftClient.getInstance());
            string = string.replace(find, replace);
        }
        return string;
    }

    public static MutableText process(Text text) {
        if (!text.getString().contains("%")) return (MutableText)text;

        MutableText result = Text
                .literal(format(text.getString()))
                .setStyle(text.getStyle());
        text.getSiblings().forEach(child -> result.append(TextProcessor.process(child)));
        return result;
    }

}
