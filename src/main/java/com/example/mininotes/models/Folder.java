package com.example.mininotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "PC_FOLDER")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "Folder_id")
    public User user;
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    public Set<Note> noteSet = new HashSet<>();

    public Folder() {}

    public Folder(long id, String name, Set<Note> noteSet){
        this.id = id;
        this.name = name;
        this.noteSet = noteSet;
    }
    public void setId(long id) {this.id = id;}
    public void setName(String name) {
        this.name = name;
    }
    public void setNoteSet(Set<Note> noteSet){this.noteSet = noteSet;}
    public void setUser(User user) {this.user = user;}

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Set<Note> getNoteSet() {return noteSet;}

    public User getUser() {return user;}
}