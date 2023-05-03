package com.example.warehousebot;

import java.util.List;

public class StudentData {
    private static String appPass, degreeId, name, email, password,degreeName;
    private static List<String> modules;

    private static boolean isStudent;

    public StudentData(){}
    public StudentData(String _appPass, String _degreeId, String _name, String _email, String _password, List<String> _modules,String _degreeName) {
        appPass = _appPass;
        degreeId = _degreeId;
        name = _name;
        email = _email;
        password = _password;
        modules = _modules;
        degreeName = _degreeName;
    }

    public static boolean isIsStudent() {
        return isStudent;
    }

    public static void setIsStudent(boolean isStudent) {
        StudentData.isStudent = isStudent;
    }

    public static  String getDegreeName() {
        return degreeName;
    }

    public static void setDegreeName(String degreeName) {
        degreeName = degreeName;
    }

    public static String getAppPass() {
        return appPass;
    }

    public static void setAppPass(String appPass) {
        appPass = appPass;
    }

    public static String getDegreeId() {
        return degreeId;
    }

    public static void setDegreeId(String degreeId) {degreeId = degreeId;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String _name) {name = _name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String _email) {
        email = _email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String _password) {
        password = _password;
    }

    public static List<String> getModules() {
        return modules;
    }

    public static void setModules(List<String> _modules) {
        modules = _modules;
    }
}
