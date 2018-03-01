package com.jiahaozhang.moodiary.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acanprince on 2017/12/9.
 *
 * @author acanprince
 * @version $Rev$
 * @des      日记本类型
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */

public class NoteType extends DataSupport {
    private int noteType;
    private String noteTypeString;
    private List<Note> noteList = new ArrayList<Note>();
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public String getNoteTypeString() {
        return noteTypeString;
    }

    public void setNoteTypeString(String noteTypeString) {
        this.noteTypeString = noteTypeString;
    }
}
