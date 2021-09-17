package lab.languages;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class SlovakLocale extends ResourceBundle {

    private Hashtable<String, String> res = null;

    @Override
    protected Object handleGetObject(String key) {
        return res.get(key);
    }

    public SlovakLocale() {
        res = new Hashtable<>();

        res.put("LoginPage.username" , "User");
        res.put("LoginPage.password" , "Heslo");
        res.put("LoginPage.login" , "vstup");
        res.put("LoginPage.register" , "Registr?cia");
        res.put("LoginPage.enterPanel" , "vstupn? panel");

        res.put("EnterGroupPage.name" , "n?zov");
        res.put("EnterGroupPage.studentsCount" , "po?et ?tudentov");
        res.put("EnterGroupPage.formOfEducation" , "forma vzdel?vania");
        res.put("EnterGroupPage.semester" , "semester");

        res.put("UpdateStudyGroup.updateButton" , "aktualiz?cia");


        res.put("EnterPersonController.personName" , "meno osoby");
        res.put("EnterPersonController.passport" , "pas");
        res.put("EnterPersonController.birthdayPicker" , "narodeniny");
        res.put("EnterPersonController.groupAdminLable" , "spr?vca skupiny");

        res.put("EnterLocationController.name" , "n?zov");
        res.put("EnterLocationController.locationLabel" , "umiestnenie");

        res.put("StudyGroupPage.nameFilter" , "n?zov filtra");
        res.put("StudyGroupPage.countFilter" , "po?et filtrov");
        res.put("StudyGroupPage.dateFilter" , "D?tum filter");
        res.put("StudyGroupPage.birthdayFilter" , "narodeniny filter");
        res.put("StudyGroupPage.semesterFilter" , "filter vzdel?vania");
        res.put("StudyGroupPage.educationFilter" , "filter n?zvu spr?vcu");
        res.put("StudyGroupPage.adminNameFilter" , "pas filter");
        res.put("StudyGroupPage.passportIdFilter" , "vymaza? filtre");
        res.put("StudyGroupPage.clearFilterButton" , "vymaza? filtre");

        res.put("simpleCommands" , "jednoduch? pr?kazy");
        res.put("removeStudyGroup" , "odstr?ni? skupinu");
        res.put("filter" , "filter");
        res.put("studyGroupCommands" , "pr?kazy pre spr?vu");
        res.put("countByGroupAdmin" , "po?et pod?a administr?tora skupiny");
        res.put("settings" , "Nastavenia");
        res.put("removeByIdPrompt" , "zadajte ID");
        res.put("removeByIdButtonLabel" , "odstr?ni?");
        res.put("removeAllByStudentsCount" , "po?et ?tudentov");
        res.put("filterLessThanSemesterEnum" , "filterlessemester");
        res.put("clear" , "clear");
        res.put("info" , "info");
        res.put("show" , "zobrazi?");
        res.put("visualize" , "visualize");
        res.put("byId" , "pod?a ID");
        res.put("byStudentsCount" , "po??tadlom ?tudentov");
        res.put("remove2" , "odstr?ni?");
        res.put("showGroupsHavingLessSem" , "zobrazi? skupiny s men??m sem");
        res.put("filterLessThenSemesterChoiceBox" , "skupiny filtrov");
        res.put("filterShowGroupsHavingLessSem" , "filter");
        res.put("generalInfoLabel" , "z?kladn? inform?cie");
        res.put("coordinatesLabel" , "koordinova?");
        res.put("addButton" , "prida?");
        res.put("removeLowerButton" , "odstr?ni? men?ie");
        res.put("addIfMinButton" , "prida?, ak min");
        res.put("updateIdButton" , "ID aktualiz?cie");
        res.put("countButton" , "po?et");
        res.put("logOutButton" , "exit");
        res.put("changeLanguageButton" , "zmeni? jazyk");
        res.put("languageComboBox" , "jazyk");
    }

    @Override
    public Enumeration<String> getKeys() {
        return res.keys();
    }
}