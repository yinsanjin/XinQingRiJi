package com.jiahaozhang.moodiary.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by acanprince on 2017/12/9.
 *
 * @author acanprince
 * @version $Rev$
 * @des      日记 本记
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */

public class Note extends DataSupport {
    private int noteId;
    private NoteType noteType;
    private String noteTitle;
    private String noteContent;
    private long lastEditorTime;
    private long createTime;
    private  int  image_id;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastEditorTime() {
        return lastEditorTime;
    }

    public void setLastEditorTime(long lastEditorTime) {
        this.lastEditorTime = lastEditorTime;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }
}
