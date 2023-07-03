package com.example.mininotes.models;

import jakarta.persistence.*;
@Entity
@Table(name = "PC_NOTE")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="title")
    private String title;
    @Column(name="body", length = 2000)
    private String body;
    @Column(name="createDateTime")
    private String createDateTime;
    @Column(name="updateDateTime")
    private String updateDateTime;
    @Column(name="accessMode")
    private AccessMode accessMode;
    @Column(name="isDelete")
    private Boolean isDelete;

    @ManyToOne
    @JoinColumn(name = "Note_id")
    public Folder folder;

    public Note() {}

    public Note(long id, String title, String body, String createDateTime,
                String updateDateTime, AccessMode accessMode, Boolean isDelete){
        this.id = id;
        this.title = title;
        this.body = body;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.accessMode = accessMode;
        this.isDelete = isDelete;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) {this.body = body;}
    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }
    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }
    public void setIsDelete(Boolean isdelete) {isDelete = isdelete;}
    public void setFolder(Folder folder) {this.folder = folder;}

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() { return body;}
    public String getCreateDateTime() {
        return createDateTime;
    }
    public String getUpdateDateTime() {
        return updateDateTime;
    }
    public AccessMode getAccessMode() {
        return accessMode;
    }
    public Boolean getIsDelete() {return isDelete;}
    public Folder getFolder() {return folder;}
}

