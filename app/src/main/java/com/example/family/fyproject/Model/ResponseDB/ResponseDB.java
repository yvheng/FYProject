package com.example.family.fyproject.Model.ResponseDB;

/**
 * Created by Family on 20/6/2018.
 */

public class ResponseDB {
    private int ID;
    private String translatedW, checkedW, studentIDT, studentIDC;

    public ResponseDB(int ID, String translatedW, String checkedW, String studentIDT, String studentIDC) {
        this.ID = ID;
        this.translatedW = translatedW;
        this.checkedW = checkedW;
        this.studentIDT = studentIDT;
        this.studentIDC = studentIDC;
    }

    public void clear(){
        this.ID = 0;
        this.translatedW = "";
        this.checkedW = "";
        this.studentIDT = "";
        this.studentIDC = "";    }

    public ResponseDB(){
    }

    public String getStudentIDT() {
        return studentIDT;
    }

    public void setStudentIDT(String studentIDT) {
        this.studentIDT = studentIDT;
    }

    public String getStudentIDC() {
        return studentIDC;
    }

    public void setStudentIDC(String studentIDC) {
        this.studentIDC = studentIDC;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTranslatedW() {
        return translatedW;
    }

    public void setTranslatedW(String translatedW) {
        this.translatedW = translatedW;
    }

    public String getCheckedW() {
        return checkedW;
    }

    public void setCheckedW(String checkedW) {
        this.checkedW = checkedW;
    }

    @Override
    public String toString() {
        return "ResponseDB{" +
                "ID=" + ID +
                ", translatedW='" + translatedW + '\'' +
                ", checkedW='" + checkedW + '\'' +
                '}';
    }
}
