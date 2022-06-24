package ua.tsopin.test.interfaces;

import android.os.Parcelable;

import ua.tsopin.test.SettingsHandler;
import ua.tsopin.test.models.Note;
import ua.tsopin.test.utils.Screens;

public interface iMain {
    SettingsHandler getSettings();

    void switchScreen(Screens scr, Parcelable obj);
    void switchScreen(Screens scr);
    void showMessage(String msg);
    void showFilterDialog();
    void showProfileDialog();
    void goBack();

    boolean doLogin(String login, String pass);
    boolean doSignUp(String login, String pass);
    void doLogout();

    void setCategory(int category);
    void loadNotes();
    void removeNote(int position, Note note);
    void addNote(Note note);
    void editNote(int position, Note note);
    void updateNote(Note note);
}
