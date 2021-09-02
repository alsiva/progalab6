package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lab.languages.UiLanguage;

import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ChangeLanguageController implements LocalizedController {

    private Consumer<ResourceBundle> changeLanguageCallback;

    public void setChangeLanguageCallback(Consumer<ResourceBundle> changeLanguageCallback) {
        this.changeLanguageCallback = changeLanguageCallback;
    }

    @FXML
    void initialize() {
        languageComboBox.getItems().setAll(UiLanguage.values());
        languageComboBox.setValue(UiLanguage.getInterfaceLanguage());
    }

    @FXML
    Button changeLanguage;

    @FXML
    ComboBox<UiLanguage> languageComboBox;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        languageComboBox.setPromptText(bundle.getString("languageComboBox"));
        changeLanguage.setText(bundle.getString("changeLanguageButton"));
    }

    public void changeLanguage() {
        UiLanguage.setLanguage(languageComboBox.getValue());
        changeLanguageCallback.accept(UiLanguage.getLanguageBundle());
    }
}
