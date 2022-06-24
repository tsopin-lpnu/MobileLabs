package ua.tsopin.test.models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import ua.tsopin.test.utils.Utils;

public class Note implements Parcelable{
    private long id;
    private String title;
    private String desc;
    private boolean is_deleted;
    private int category;
    private int category_color;
    private Date created_at;
    private Date updated_at;

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            long id = source.readLong();
            String title = source.readString();
            String desc = source.readString();
            boolean is_deleted = (source.readInt() != 0);
            int category = source.readInt();
            int category_color = source.readInt();
            Date created_at = new Date(source.readLong());
            Date updated_at = new Date(source.readLong());

            return new Note(id, title, desc, is_deleted, category, category_color, created_at, updated_at);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Note(long id, String title, String desc, boolean is_deleted, int category, int category_color, Date created_at, Date updated_at) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.is_deleted = is_deleted;
        this.category = category;
        this.category_color = category_color;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Note(String title, String desc, int category) {
        Date now = new Date();

        this.id = 0;
        this.title = title;
        this.desc = desc;
        this.is_deleted = false;
        this.setCategory(category);
        //this.category = category;
        //this.category_color = Color.WHITE;
        this.created_at = now;
        this.updated_at = now;
    }

    public Note() {
        Date now = new Date();

        this.id = 0;
        this.title = "";
        this.desc = "";
        this.is_deleted = false;
        this.category = 0;
        this.category_color = Color.WHITE;
        this.created_at = now;
        this.updated_at = now;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
        this.category_color = Utils.getColorCategory(category);
    }

    public int getCategory_color() {
        return category_color;
    }

    public void setCategory_color(int category_color) {
        this.category_color = category_color;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeInt(((is_deleted)?1:0));
        dest.writeInt(category);
        dest.writeInt(category_color);
        dest.writeLong(created_at.getTime());
        dest.writeLong(updated_at.getTime());
    }
}
