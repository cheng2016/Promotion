package com.cds.promotion.data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by chengzj on 2018/9/26.
 */
@Entity
public class SMessage {
    @Id(autoincrement = true)
    private Long id;

    private String token;

    private String userId;

    private String uid;

    private String msgId;

    private String msgType;

    private String title;

    private String content;

    private int isNew;

    private String tailtime;

    @Generated(hash = 57848717)
    public SMessage(Long id, String token, String userId, String uid, String msgId,
            String msgType, String title, String content, int isNew,
            String tailtime) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.uid = uid;
        this.msgId = msgId;
        this.msgType = msgType;
        this.title = title;
        this.content = content;
        this.isNew = isNew;
        this.tailtime = tailtime;
    }

    @Generated(hash = 760968538)
    public SMessage() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTailtime() {
        return this.tailtime;
    }

    public void setTailtime(String tailtime) {
        this.tailtime = tailtime;
    }

    public int getIsNew() {
        return this.isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

 }
