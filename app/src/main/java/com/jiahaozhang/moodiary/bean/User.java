package com.jiahaozhang.moodiary.bean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acanprince on 2017/11/27.
 *
 * @author acanprince
 * @version 1
 * @des     用户表
 * @updateAuthor aclearzhang
 * @updateDes ${TODO}
 */

public class User extends DataSupport {
    static  int count;
    private int id;
    private String nickname;
    private String protraint;
    private String password;
    private String email;
    private int phone;
    private List<NoteType> noteTypeList = new ArrayList<NoteType>();

    public List<NoteType> getNoteTypeList() {
        return noteTypeList;
    }

    public void setNoteTypeList(List<NoteType> noteTypeList) {
        this.noteTypeList = noteTypeList;
    }

    public User() {
        count += 1;
        this.id = count;
    }

    public int getId() {
       return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProtraint() {
        return protraint;
    }

    public void setProtraint(String protraint) {
        this.protraint = protraint;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
