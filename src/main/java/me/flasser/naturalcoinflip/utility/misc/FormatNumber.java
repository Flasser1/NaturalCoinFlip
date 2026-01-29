package me.flasser.naturalcoinflip.utility.misc;

import eu.okaeri.platform.core.annotation.Component;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class FormatNumber {
    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
    static String[] suffixArr = {"K","M","B","T","Qa","Qi","Sx","Sp","Oc","No","Dc","Udc","Ddc","Tdc","Qadc","Qidc","Sxdc","Spdc","Ocdc","Nmdc","Vg","Uvg","Dvg","Tvg","Qavg","Qivg","Sxvg","Spvg","Ovg","Nvg","Tg","Utg","Dtg"};
    static {
        double value = 1_000D;

        for (int i = 0; i < suffixArr.length; i++) {
            suffixes.put(value, suffixArr[i]);
            value *= 1_000D;
        }
    }
    public String format(double value) {
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return String.valueOf((long) value);

        Map.Entry<Double, String> e = suffixes.floorEntry(value);
        double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10.0);
        boolean hasDecimal = truncated < 100;

        return hasDecimal
                ? String.format("%.2f%s", truncated / 10.0, suffix)
                : String.format("%.0f%s", truncated / 10.0, suffix);
    }

    public Double formatDou(String value) {
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {}

        String suffixPattern = String.join("|", suffixArr);
        Pattern pattern = Pattern.compile("^(\\d+(?:\\.\\d+)?)(?:" + suffixPattern + ")?$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);

        if (!matcher.matches()) return null;

        String numberPart = matcher.group(1);
        String suffixPart = value.substring(numberPart.length());

        int index = -1;
        for (int i = 0; i < suffixArr.length; i++) {
            if (suffixArr[i].equalsIgnoreCase(suffixPart)) {
                index = i+1;
                break;
            }
        }

        if (index == -1) return null;

        return Double.parseDouble(numberPart)*Math.pow(1000, index);
    }

    public Integer formatInt(String value) {
        Double result = formatDou(value);
        if (result == null) return null;

        if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) return null;

        return result.intValue();
    }
}
