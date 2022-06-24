package ua.tsopin.test.utils;

import android.database.Cursor;
import android.graphics.Color;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.tsopin.test.models.Note;

public class Utils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
    private static final String[] colors = new String[] {
                    "#3498DC",
                    "#FAE033",
                    "#50C878",
                    "#D32730",
                    "#ED9121",
                    "#C9A0DC",
            };

    public static int getColorCategory(int cat) {
        if(cat < 0 || cat > colors.length){
            return Color.BLACK;
        }

        return Color.parseColor(colors[cat]);
    }
//    public static String getNow() {
//        return dateFormat.format(new Date());
//    }

    public static long getNow() {
        return System.currentTimeMillis();
    }

    public static String formateDate(Date date) {
        return dateFormat.format(date);
    }

    public static Note loadNoteFromCursor(Cursor cursor) {
        Note note = new Note();

        note.setId(cursor.getLong(0));
        note.setTitle(cursor.getString(1));
        note.setDesc(cursor.getString(2));
        note.setIs_deleted(cursor.getInt(3) != 0);
        note.setCategory(cursor.getInt(4));
        note.setCategory_color(Color.parseColor(cursor.getString(5)));
        note.setCreated_at(new Date(cursor.getLong(6)));
        note.setUpdated_at(new Date(cursor.getLong(7)));

        return note;
    }


    public static String getTitleFromDesc(String desc) {
        String title = "Title";
        String trimString;

        for (String line : desc.split("\n")) {
            trimString = line.trim();
            if (!trimString.isEmpty()) {
                if (trimString.length() > 50) {
                    trimString = trimString.substring(0, 49);
                }
                title = trimString;
                break;
            }
        }

        return title;
    }
}
