package lab.languages;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class EnglishLocale extends ResourceBundle {

    private Hashtable<String, String> res = null;

    public EnglishLocale() {
        res = new Hashtable<String, String>();
        res.put("LoginPage.username", "Username");
        res.put("LoginPage.password", "Password");
        res.put("LoginPage.login","Log in");
        res.put("LoginPage.register", "Register");
        res.put("LoginPage.enterPanel", "EnterPanel");

        res.put("EnterGroupPage.name", "name");
        res.put("EnterGroupPage.studentsCount", "studentsCount");
        res.put("EnterGroupPage.formOfEducation", "formOfEducation");
        res.put("EnterGroupPage.semester" , "semester");

        res.put("UpdateStudyGroup.updateButton" , "update");

        res.put("EnterPersonController.personName" , "personName");
        res.put("EnterPersonController.passport" , "passport");
        res.put("EnterPersonController.birthdayPicker" , "birthday");
        res.put("EnterPersonController.groupAdminLable" , "groupAdmin");

        res.put("EnterLocationController.name" , "name");
        res.put("EnterLocationController.locationLabel" , "location");

        res.put("StudyGroupPage.nameFilter" , "nameFilter");
        res.put("StudyGroupPage.countFilter" , "countFilter");
        res.put("StudyGroupPage.dateFilter" ,  "dateFilter");
        res.put("StudyGroupPage.birthdayFilter" , "birthdayFilter");
        res.put("StudyGroupPage.semesterFilter" , "semesterFilter");
        res.put("StudyGroupPage.educationFilter" , "educationFilter");
        res.put("StudyGroupPage.adminNameFilter" , "adminNameFilter");
        res.put("StudyGroupPage.passportIdFilter" , "passportIdFilter");
        res.put("StudyGroupPage.clearFilterButton" , "clearFilter");
        
        res.put("simpleCommands" , "simple commands");
        res.put("removeStudyGroup" , "remove study group");
        res.put("filter" , "filter");
        res.put("studyGroupCommands", "study group commands");
        res.put("countByGroupAdmin", "count by group admin");
        res.put("settings", "settings");
        res.put("removeByIdPrompt", "enter id");
        res.put("removeByIdButtonLabel", "remove by id");
        res.put("removeAllByStudentsCount", "removeAllByStudentsCount");
        res.put("filterLessThanSemesterEnum", "filterLessThanSemester");
        res.put("clear", "clear");
        res.put("info", "info");
        res.put("show", "show");
        res.put("visualize", "visualize");
        res.put("byId", "byId");
        res.put("byStudentsCount" , "byStudentsCount");
        res.put("remove2", "remove");
        res.put("showGroupsHavingLessSem", "showGroupsHavingLessSem");
        res.put("filterLessThenSemesterChoiceBox", "filterLessThenSemesterChoiceBox");
        res.put("filterShowGroupsHavingLessSem", "filterShowGroupsHavingLessSem");
        res.put("generalInfoLabel", "generalInfo");
        res.put("coordinatesLabel", "coordinates");
        res.put("addButton", "add");
        res.put("addIfMinButton", "addIfMin");
        res.put("removeLowerButton", "removeLower");
        res.put("updateIdButton", "updateID");
        res.put("countButton", "count");
        res.put("logOutButton", "logOut");
        res.put("changeLanguageButton", "changeLanguage");
        res.put("languageComboBox", "language");
    }

    @Override
    protected Object handleGetObject(String key) {
        return res.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return res.keys();
    }


}
