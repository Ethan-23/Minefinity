package org.evasive.me.minefinity.core.utils.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class EconNumberUtils {

    private static List<Double> balanceAmounts = List.of(1000000000000000.0, 1000000000000.0, 1000000000.0, 1000000.0, 1000.0);
    private static List<String> balanceSuffix = List.of("q", "t", "b", "m", "k");

    public static double round(double value) {
        int places = 2;
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static String balanceSuffix(double value) {


        for  (int i = 0; i < balanceAmounts.size(); i++) {
            double number = balanceAmounts.get(i);
            if(number / value >= 10 || number > value) continue;

            double displayNumber = round(value/number);
            return new DecimalFormat("###.##").format(displayNumber) + balanceSuffix.get(i);
        }

        return String.valueOf(value);
    }

    public static boolean hasSuffix (String amount){
        String suffix = amount.substring(amount.length() - 1);
        return balanceSuffix.contains(suffix);
    }

    public static double suffixToNumber(String amount) {
        String suffix = amount.substring(amount.length() - 1);
        double number;

        try {
            number = Double.parseDouble(amount.substring(0, amount.length() - 1));
        }catch (NumberFormatException e) {
            return -1;
        }
        return balanceAmounts.get(balanceSuffix.indexOf(suffix)) * number;
    }

    public static String wholeBalanceDisplay(double value){
        return new DecimalFormat("#,###.##").format(value);
    }

}
