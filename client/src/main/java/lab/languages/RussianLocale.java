package lab.languages;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class RussianLocale extends ResourceBundle {

    private Hashtable<String, String> res = null;

    @Override
    protected Object handleGetObject(String key) {
        return res.get(key);
    }

    public RussianLocale() {
        res = new Hashtable<>();
        res.put("LoginPage.username" , "Пользователь");
        res.put("LoginPage.password" , "Пароль");
        res.put("LoginPage.login" , "Вход");
        res.put("LoginPage.register" , "Регистрация");
        res.put("LoginPage.enterPanel" , "Входная панель");

        res.put("EnterGroupPage.name" , "имя");
        res.put("EnterGroupPage.studentsCount" , "количество студентов");
        res.put("EnterGroupPage.formOfEducation" , "форма образования");
        res.put("EnterGroupPage.semester" , "семестр");

        res.put("UpdateStudyGroup.updateButton" , "обновить");


        res.put("EnterPersonController.personName" , "имя персоны");
        res.put("EnterPersonController.passport" , "паспорт");
        res.put("EnterPersonController.birthdayPicker" , "день рождения");
        res.put("EnterPersonController.groupAdminLable" , "админ группы");

        res.put("EnterLocationController.name" , "имя");
        res.put("EnterLocationController.locationLabel" , "локация");

        res.put("StudyGroupPage.nameFilter" , "фильтр имени");
        res.put("StudyGroupPage.countFilter" , "фильтр количества");
        res.put("StudyGroupPage.dateFilter" , "фильтр даты");
        res.put("StudyGroupPage.birthdayFilter" , "фильтр дня рождения");
        res.put("StudyGroupPage.semesterFilter" , "фильтр семестра");
        res.put("StudyGroupPage.educationFilter" , "фильтр образования");
        res.put("StudyGroupPage.adminNameFilter" , "фильтр имени админа");
        res.put("StudyGroupPage.passportIdFilter" , "фильтр паспорта");
        res.put("StudyGroupPage.clearFilterButton" , "очистить фильтры");

        res.put("simpleCommands" , "Простые команды");
        res.put("removeStudyGroup" , "Удалить группу");
        res.put("filter" , "Фильтр");
        res.put("studyGroupCommands" , "Команды управления группой");
        res.put("countByGroupAdmin" , "Посчитать по админу группы");
        res.put("settings" , "Настройки");
        res.put("removeByIdPrompt" , "введите айди");
        res.put("removeByIdButtonLabel" , "убрать");
        res.put("removeAllByStudentsCount" , "количество студентов");
        res.put("filterLessThanSemesterEnum" , "фильтрМеньшеСеместр");
        res.put("clear" , "очистить");
        res.put("info" , "инфа");
        res.put("show" , "показать");
        res.put("visualize" , "визуализировать");
        res.put("byId" , "по айди");
        res.put("byStudentsCount" , "по счётчику студентов");
        res.put("remove2" , "убрать");
        res.put("showGroupsHavingLessSem" , "показать группы имеющие меньший сем");
        res.put("filterLessThenSemesterChoiceBox" , "отфильтровать группы");
        res.put("filterShowGroupsHavingLessSem" , "фильтровать");
        res.put("generalInfoLabel" , "основная информация");
        res.put("coordinatesLabel" , "координаты");
        res.put("addButton" , "добавить");
        res.put("removeLowerButton" , "убрать меньшие");
        res.put("addIfMinButton" , "добавить если мин");
        res.put("updateIdButton" , "обновить айди");
        res.put("countButton" , "посчитать");
        res.put("logOutButton" , "выйти");
        res.put("changeLanguageButton" , "поменять язык");
        res.put("languageComboBox" , "язык");
    }

    @Override
    public Enumeration<String> getKeys() {
        return res.keys();
    }
}
