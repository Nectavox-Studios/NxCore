package com.nectavox.nxcore.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberFormatUtil {

    public static String format(long number) {

        if (number >= 1_000_000_000_000L)
            return trim(number / 1_000_000_000_000.0) + "T";

        if (number >= 1_000_000_000L)
            return trim(number / 1_000_000_000.0) + "B";

        if (number >= 1_000_000L)
            return trim(number / 1_000_000.0) + "M";

        if (number >= 1_000L)
            return trim(number / 1_000.0) + "K";

        return String.valueOf(trim(number));
    }

    public static String format(double number) {

        if (number >= 1_000_000_000_000L)
            return trim(number / 1_000_000_000_000.0) + "T";

        if (number >= 1_000_000_000L)
            return trim(number / 1_000_000_000.0) + "B";

        if (number >= 1_000_000L)
            return trim(number / 1_000_000.0) + "M";

        if (number >= 1_000L)
            return trim(number / 1_000.0) + "K";

        return String.valueOf(trim(number));
    }

    public static long parseAbbreviatedNumber(String input) {
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }

        input = input.trim().toLowerCase();

        Pattern pattern = Pattern.compile("^([\\d.]+)\\s*([kmb]?)$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            double numberPart = Double.parseDouble(matcher.group(1));
            String suffix = matcher.group(2);

            if (numberPart < 0) {
                throw new IllegalArgumentException("Number is to small : " + input);
            }

            return switch (suffix) {
                case "k" -> (long) (numberPart * 1_000);
                case "m" -> (long) (numberPart * 1_000_000);
                case "b" -> (long) (numberPart * 1_000_000_000);
                default -> (long) numberPart;
            };

        }

        throw new IllegalArgumentException("Invalid Number Format : " + input);
    }

    private static String trim(double value) {
        if (value == (long) value)
            return String.valueOf((long) value);
        return String.format("%.2f", value);
    }
}