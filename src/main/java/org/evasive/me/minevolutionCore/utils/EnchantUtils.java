package org.evasive.me.minevolutionCore.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EnchantUtils {


    public static <E extends Enum<E>> String enchantMapToString(Map<E, Integer> enchantMap){
        StringBuilder enchantList = new StringBuilder();
        for (Map.Entry<E, Integer> entry : enchantMap.entrySet()) {
            enchantList.append(entry.getKey().name()) // save enum name
                    .append(":")
                    .append(entry.getValue())
                    .append(",");
        }
        if (!enchantList.isEmpty()) enchantList.setLength(enchantList.length() - 1);
        return enchantList.toString();
    }

    public static <E extends Enum<E>> Map<E, Integer> stringToEnumMap(String str, Class<E> enumClass) {
        Map<E, Integer> map = new HashMap<>();
        if (str == null || str.isEmpty()) return map;

        String[] entries = str.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.split(":");
            if (keyValue.length == 2) {
                E key = Enum.valueOf(enumClass, keyValue[0]); // reconstruct enum
                Integer value = Integer.parseInt(keyValue[1]);
                map.put(key, value);
            }
        }
        return map;
    }

    public static String intToRoman(int num) {
        int[] values =    {1000, 900, 500, 400, 100, 90,  50,  40,  10, 9,   5,  4,   1};
        String[] symbols = {"M",  "CM","D", "CD","C", "XC","L", "XL","X","IX","V","IV","I"};

        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                roman.append(symbols[i]);
            }
        }

        return roman.toString();
    }

    private static final Random random = new Random();

    public static boolean calculateChance(float baseChance, int level) {
        float totalChance = baseChance * level;

        // Cap at 100% to avoid overflow
        if (totalChance >= 1.0f) {
            return true;
        }

        return random.nextFloat() < totalChance;
    }

    public static boolean calculateChance(float baseChance) {
        // Cap at 100% to avoid overflow
        if (baseChance >= 1.0f) {
            return true;
        }

        return random.nextFloat() < baseChance;
    }
}
