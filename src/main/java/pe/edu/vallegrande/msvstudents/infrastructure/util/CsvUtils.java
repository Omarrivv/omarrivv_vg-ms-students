package pe.edu.vallegrande.msvstudents.infrastructure.util;

public final class CsvUtils {

    private CsvUtils() {}

    public static String toCsvValue(Object value) {
        if (value == null) {
            return "";
        }
        String str = String.valueOf(value);
        boolean mustQuote = str.contains(",") || str.contains("\n") || str.contains("\r") || str.contains("\"");
        if (str.contains("\"")) {
            str = str.replace("\"", "\"\"");
        }
        return mustQuote ? '"' + str + '"' : str;
    }

    public static String joinCsv(Object... values) {
        if (values == null || values.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(toCsvValue(values[i]));
        }
        return builder.toString();
    }
}


