package com.example.family.fyproject.Model.Word;

/**
 * Created by Family on 9/6/2018.
 */

public class Word {
    private int ID;
    private String original;
    private int translated, checked, editing;

    public Word(int ID, String original,int translated, int checked, int editing) {
        this.ID = ID;
        this.original = original;
        this.translated = translated;
        this.checked = checked;
        this.editing = editing;
    }

    public Word(Word anotherWord) {
        this.ID = anotherWord.getID();
        this.original = anotherWord.getOriginal();
        this.editing = anotherWord.getEditing();
        this.translated = anotherWord.getTranslated();
        this.checked = anotherWord.getChecked();
    }

    public Word() {
    }

    public void clear(){
        this.ID = 0;
        this.original = "";
        this.translated = 0;
        this.checked = 0;
        this.editing = 0;    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public int getTranslated() {
        return translated;
    }

    public void setTranslated(int translated) {
        this.translated = translated;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getEditing() {
        return editing;
    }

    public void setEditing(int editing) {
        this.editing = editing;
    }

    @Override
    public String toString() {
        return "Data details :- /n"+
                "ID : "+ID+"/n"+
                "Original word : "+original+"/n"+
                "Translated? : "+translated+"/n"+
                "Checked? : "+checked+"/n"+
                "Editing? : "+editing+"/n";
    }
}
