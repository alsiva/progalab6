package lab.languages;

import java.util.Locale;
import java.util.ResourceBundle;

public enum UiLanguage {
    RUS,
    ENG;

    public static ResourceBundle getLanguageBundle() {
        if (interfaceLanguage == UiLanguage.RUS) {
            return ResourceBundle.getBundle("text", new Locale("ru"));
        }

        return ResourceBundle.getBundle("text", new Locale("en"));
    }

    private static UiLanguage interfaceLanguage = UiLanguage.ENG;

    public static void setLanguage(UiLanguage language) {
        interfaceLanguage = language;
    }

    public static UiLanguage getInterfaceLanguage() {
        return interfaceLanguage;
    }
}
