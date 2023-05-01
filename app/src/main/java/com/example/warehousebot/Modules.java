package com.example.warehousebot;

public class Modules {
    String details,moduleCode,name,colorCode;

    public Modules(){}

    public Modules(String details, String moduleCode, String name, String colorCode) {
        this.details = details;
        this.moduleCode = moduleCode;
        this.name = name;
        this.colorCode = colorCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
