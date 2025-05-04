package src.utils;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class A03_Formating {
    public static void main(String[] args) {
        /*
        Calendar é uma classe Java que representa um calendário e fornece métodos para manipular datas e horas.
        A classe Calendar é abstrata e não pode ser instanciada diretamente. Em vez disso, você deve usar o metodo
        estático getInstance() para obter uma instância de Calendar. O metodo getInstance() retorna um objeto
        Calendar que representa a data e hora atuais, de acordo com o fuso horário e a localidade do sistema.
         */
        Calendar calendar = Calendar.getInstance();

        DateFormat[] dateFormats = new DateFormat[7];
        dateFormats[0] = DateFormat.getInstance();
        dateFormats[1] = DateFormat.getDateInstance();
        dateFormats[2] = DateFormat.getDateTimeInstance();
        dateFormats[3] = DateFormat.getDateInstance(DateFormat.SHORT);
        dateFormats[4] = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateFormats[5] = DateFormat.getDateInstance(DateFormat.LONG);
        dateFormats[6] = DateFormat.getDateInstance(DateFormat.FULL);

        for (DateFormat df : dateFormats) {
            System.out.println(df.format(calendar.getTime()));
        }

        /*
         */

        String[][] locales = {
                {"Brasil", "pt", "BR"},
                {"Portugal", "pt", "PT"},
                {"Reino Unido", "en", "GB"},
                {"Estados Unidos", "en", "US"},
                {"Alemanha", "de", "DE"},
                {"Espanha", "es", "ES"},
                {"Itália", "it", "IT"},
                {"Holanda", "nl", "NL"},
                {"Japão", "ja", "JP"},
                {"China", "zh", "CN"},
                {"Coreia do Sul", "ko", "KR"},
                {"Rússia", "ru", "RU"},
                {"Índia", "hi", "IN"},
                {"Argentina", "es", "AR"},
        };

        System.out.println("\n\n");
        System.out.println(Arrays.toString(Locale.getISOCountries()));
        System.out.println(Arrays.toString(Locale.getISOLanguages()));

        System.out.println("\n\n Dates");
        for (String[] locale : locales) {
            printDateFormat(locale[0], new Locale(locale[1], locale[2]));
        }

        System.out.println("\n\n Numbers");
        for (String[] locale : locales) {
            printNumberFormat(locale[0], new Locale(locale[1], locale[2]));
        }

        System.out.println("\n\n Currency");
        for (String[] locale : locales) {
            printCurrencyFormat(locale[0], new Locale(locale[1], locale[2]));
        }

        /*
        SimpleDateFormat é uma subclasse de DateFormat que permite formatar e analisar datas em um formato específico.
         */
        String[] patterns = {
                "dd/MM/yyyy",
                "dd-MM-yyyy",
                "yyyy/MM/dd",
                "yyyy-MM-dd",
                "dd MMMM yyyy",
                "dd MMM yyyy",
                "dd/MMM/yyyy",
                "dd/MMM/yy",
                "dd-MMM-yyyy",
                "dd-MMM-yy",
                "'Data atual:' dd/MM/yyyy",
        };
        System.out.println("\n\nSimpleDateFormat");
        for (String pattern : patterns) {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            System.out.println(dateFormat.format(calendar.getTime()));
        }
    }

    public static void printDateFormat(String countryName, Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
        System.out.println("Data e hora em " + countryName + ": " +  dateFormat.format(calendar.getTime()));
    }

    public static void printNumberFormat(String countryName, Locale locale) {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        double valor = 10_123_000.2130;
        System.out.println("10_123_000.2130" + " em " + countryName + ": " + numberFormat.format(valor));
    }

    public static void printCurrencyFormat(String countryName, Locale locale) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        double valor = 10_123_000.2130;
        System.out.println("10_123_000.2130" + " em " + countryName + ": " + currencyFormat.format(valor));
    }
}
