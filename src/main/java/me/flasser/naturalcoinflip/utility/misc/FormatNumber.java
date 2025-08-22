package me.flasser.naturalcoinflip.utility.misc;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class FormatNumber {
    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();
    static {
        String[] suffixArr = {"K","M","B","T","Qa","Qi","Sx","Sp","Oc","No","Dc","Udc","Ddc","Tdc","Qadc","Qidc","Sxdc","Spdc","Ocdc","Nmdc","Vg","Uvg","Dvg","Tvg","Qavg","Qivg","Sxvg","Spvg","Ovg","Nvg","Tg","Utg","Dtg"};
        double value = 1_000D;

        for (int i = 0; i < suffixArr.length; i++) {
            suffixes.put(value, suffixArr[i]);
            value *= 1_000D;
        }
    }
    public static String format(double value) {
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return String.valueOf((long) value);

        Map.Entry<Double, String> e = suffixes.floorEntry(value);
        double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10.0);
        boolean hasDecimal = truncated < 100 && (truncated / 10.0) != (truncated / 10.0);

        return hasDecimal
                ? String.format("%.2f%s", truncated / 10.0, suffix)
                : String.format("%.0f%s", truncated / 10.0, suffix);
    }
}
