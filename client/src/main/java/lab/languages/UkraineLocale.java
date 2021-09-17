package lab.languages;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class UkraineLocale extends ResourceBundle {

    private Hashtable<String, String> res = null;

    @Override
    protected Object handleGetObject(String key) {
        return res.get(key);
    }

    public UkraineLocale() {
        res = new Hashtable<>();

        res.put("LoginPage.username" , "Користувач");
        res.put("LoginPage.password" , "Пароль");
        res.put("LoginPage.login" , "вхід");
        res.put("LoginPage.register" , "реєстрація");
        res.put("LoginPage.enterPanel" , "вхідна панель");

        res.put("EnterGroupPage.name" , "ім'я");
        res.put("EnterGroupPage.studentsCount" , "кількість студентів");
        res.put("EnterGroupPage.formOfEducation" , "форма освіти");
        res.put("EnterGroupPage.semester" , "семестр");

        res.put("UpdateStudyGroup.updateButton" , "оновити");


        res.put("EnterPersonController.personName" , "ім'я персони");
        res.put("EnterPersonController.passport" , "паспорт");
        res.put("EnterPersonController.birthdayPicker" , "день народження");
        res.put("EnterPersonController.groupAdminLable" , "адмін групи");

        res.put("EnterLocationController.name" , "ім'я");
        res.put("EnterLocationController.locationLabel" , "локація");

        res.put("StudyGroupPage.nameFilter" , "фільтр імені");
        res.put("StudyGroupPage.countFilter" , "фільтр кількості");
        res.put("StudyGroupPage.dateFilter" , "фільтр дати");
        res.put("StudyGroupPage.birthdayFilter" , "фільтр дня народження");
        res.put("StudyGroupPage.semesterFilter" , "фільтр семестру");
        res.put("StudyGroupPage.educationFilter" , "фільтр освіти");
        res.put("StudyGroupPage.adminNameFilter" , "фільтр імені адміна");
        res.put("StudyGroupPage.passportIdFilter" , "фільтр паспорта");
        res.put("StudyGroupPage.clearFilterButton" , "Очистити фільтри");

        res.put("simpleCommands" , "прості команди");
        res.put("removeStudyGroup" , "Видалити групу");
        res.put("filter" , "фільтр");
        res.put("studyGroupCommands" , "команди управління групою");
        res.put("countByGroupAdmin" , "порахувати по Адміну групи");
        res.put("settings" , "Налаштування");
        res.put("removeByIdPrompt" , "введіть айді");
        res.put("removeByIdButtonLabel" , "прибрати");
        res.put("removeAllByStudentsCount" , "кількість студентів");
        res.put("filterLessThanSemesterEnum" , "фільтр менше Семестр");
        res.put("clear" , "Очистити");
        res.put("info" , "інфа");
        res.put("show" , "показати");
        res.put("visualize" , "візуалізувати");
        res.put("byId" , "по айді");
        res.put("byStudentsCount" , "за лічильником студентів");
        res.put("remove2" , "прибрати");
        res.put("showGroupsHavingLessSem" , "показати групи мають менший сем");
        res.put("filterLessThenSemesterChoiceBox" , "відфільтрувати групи");
        res.put("filterShowGroupsHavingLessSem" , "фільтрувати");
        res.put("generalInfoLabel" , "основна інформація");
        res.put("coordinatesLabel" , "координати");
        res.put("addButton" , "додати");
        res.put("removeLowerButton" , "прибрати менш");
        res.put("addIfMinButton" , "додати якщо хв");
        res.put("updateIdButton" , "оновити айді");
        res.put("countButton" , "порахувати");
        res.put("logOutButton" , "вийти");
        res.put("changeLanguageButton" , "поміняти мову");
        res.put("languageComboBox" , "мова");
    }

    @Override
    public Enumeration<String> getKeys() {
        return res.keys();
    }
}