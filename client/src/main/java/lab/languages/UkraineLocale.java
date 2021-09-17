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

        res.put("LoginPage.username" , "����������");
        res.put("LoginPage.password" , "������");
        res.put("LoginPage.login" , "����");
        res.put("LoginPage.register" , "���������");
        res.put("LoginPage.enterPanel" , "������ ������");

        res.put("EnterGroupPage.name" , "��'�");
        res.put("EnterGroupPage.studentsCount" , "������� ��������");
        res.put("EnterGroupPage.formOfEducation" , "����� �����");
        res.put("EnterGroupPage.semester" , "�������");

        res.put("UpdateStudyGroup.updateButton" , "�������");


        res.put("EnterPersonController.personName" , "��'� �������");
        res.put("EnterPersonController.passport" , "�������");
        res.put("EnterPersonController.birthdayPicker" , "���� ����������");
        res.put("EnterPersonController.groupAdminLable" , "���� �����");

        res.put("EnterLocationController.name" , "��'�");
        res.put("EnterLocationController.locationLabel" , "�������");

        res.put("StudyGroupPage.nameFilter" , "������ ����");
        res.put("StudyGroupPage.countFilter" , "������ �������");
        res.put("StudyGroupPage.dateFilter" , "������ ����");
        res.put("StudyGroupPage.birthdayFilter" , "������ ��� ����������");
        res.put("StudyGroupPage.semesterFilter" , "������ ��������");
        res.put("StudyGroupPage.educationFilter" , "������ �����");
        res.put("StudyGroupPage.adminNameFilter" , "������ ���� �����");
        res.put("StudyGroupPage.passportIdFilter" , "������ ��������");
        res.put("StudyGroupPage.clearFilterButton" , "�������� �������");

        res.put("simpleCommands" , "����� �������");
        res.put("removeStudyGroup" , "�������� �����");
        res.put("filter" , "������");
        res.put("studyGroupCommands" , "������� ��������� ������");
        res.put("countByGroupAdmin" , "���������� �� ����� �����");
        res.put("settings" , "������������");
        res.put("removeByIdPrompt" , "������ ���");
        res.put("removeByIdButtonLabel" , "��������");
        res.put("removeAllByStudentsCount" , "������� ��������");
        res.put("filterLessThanSemesterEnum" , "������ ����� �������");
        res.put("clear" , "��������");
        res.put("info" , "����");
        res.put("show" , "��������");
        res.put("visualize" , "�����������");
        res.put("byId" , "�� ���");
        res.put("byStudentsCount" , "�� ���������� ��������");
        res.put("remove2" , "��������");
        res.put("showGroupsHavingLessSem" , "�������� ����� ����� ������ ���");
        res.put("filterLessThenSemesterChoiceBox" , "������������� �����");
        res.put("filterShowGroupsHavingLessSem" , "�����������");
        res.put("generalInfoLabel" , "������� ����������");
        res.put("coordinatesLabel" , "����������");
        res.put("addButton" , "������");
        res.put("removeLowerButton" , "�������� ����");
        res.put("addIfMinButton" , "������ ���� ��");
        res.put("updateIdButton" , "������� ���");
        res.put("countButton" , "����������");
        res.put("logOutButton" , "�����");
        res.put("changeLanguageButton" , "������� ����");
        res.put("languageComboBox" , "����");
    }

    @Override
    public Enumeration<String> getKeys() {
        return res.keys();
    }
}