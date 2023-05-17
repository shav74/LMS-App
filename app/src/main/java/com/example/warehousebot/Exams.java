package com.example.warehousebot;

import androidx.fragment.app.Fragment;

public class Exams extends Fragment {

    String examName, dateReleased, description;

    public Exams(){}

    public Exams(String examName, String dateReleased, String description) {
        this.examName = examName;
        this.dateReleased = dateReleased;
        this.description = description;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getDateReleased() {
        return dateReleased;
    }

    public void setDateReleased(String dateReleased) {
        this.dateReleased = dateReleased;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
