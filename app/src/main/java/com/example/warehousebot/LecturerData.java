package com.example.warehousebot;

import java.util.List;

public class LecturerData {
    private static String appPass,name,email,password;
    private static List<String> teachingModules;
    private static boolean isLecturer;

    public LecturerData(){}
    public LecturerData(String _appPass, String _name, String _email, String _password, List<String> _teachingModules) {
        appPass = _appPass;
        name = _name;
        email = _email;
        password = _password;
        teachingModules = _teachingModules;
    }

    public static String getAppPass() {
        return appPass;
    }

    public static void setAppPass(String appPass) {
        LecturerData.appPass = appPass;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        LecturerData.name = name;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        LecturerData.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        LecturerData.password = password;
    }

    public static List<String> getTeachingModules() {
        return teachingModules;
    }

    public static void setTeachingModules(List<String> teachingModules) {
        LecturerData.teachingModules = teachingModules;
    }

    public static boolean isIsLecturer() {
        return isLecturer;
    }

    public static void setIsLecturer(boolean isLecturer) {
        LecturerData.isLecturer = isLecturer;
    }
}
