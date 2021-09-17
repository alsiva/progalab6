package lab.languages;

import java.util.Locale;
import java.util.ResourceBundle;

public enum UiLanguage {
    RUS,
    SLO,
    UKR,
    ENG;

    public static ResourceBundle getLanguageBundle() {
        try {
            if (interfaceLanguage == UiLanguage.RUS) {
                return ResourceBundle.getBundle("lab.languages.RussianLocale", new Locale("ru"));
            } else if (interfaceLanguage == UiLanguage.SLO) {
                return ResourceBundle.getBundle("lab.languages.SlovakLocale", new Locale("sk"));
            } else if (interfaceLanguage == UiLanguage.UKR) {
                return ResourceBundle.getBundle("lab.languages.UkraineLocale", new Locale("uk"));
            }

            return ResourceBundle.getBundle("lab.languages.EnglishLocale", new Locale("en"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static UiLanguage interfaceLanguage = UiLanguage.ENG;

    public static void setLanguage(UiLanguage language) {
        interfaceLanguage = language;
    }

    public static UiLanguage getInterfaceLanguage() {
        return interfaceLanguage;
    }
}
