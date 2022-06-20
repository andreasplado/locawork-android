package org.locawork.util;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

public class CurrencyUtil {
    public static SortedMap<Currency, Locale> currencyLocaleMap = new TreeMap(new Comparator<Currency>() {
        public int compare(Currency c1, Currency c2) {
            return c1.getCurrencyCode().compareTo(c2.getCurrencyCode());
        }
    });

    static {
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                currencyLocaleMap.put(Currency.getInstance(locale), locale);
            } catch (Exception e) {
            }
        }
    }

    public static String getCurrencySymbol(String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        PrintStream printStream = System.out;
        printStream.println(currencyCode + ":-" + currency.getSymbol((Locale) currencyLocaleMap.get(currency)));
        return currency.getSymbol((Locale) currencyLocaleMap.get(currency));
    }
}
