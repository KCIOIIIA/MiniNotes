package com.example.mininotes.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "FOLDER")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name")
    private String name;
    //@ManyToOne
    //@JoinColumn(name = "Folder_id")
    //public User user;
    @OneToMany(cascade = CascadeType.ALL)
    public Set<Note> noteSet = new HashSet<>();

    public Folder() {}
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNoteSet(Set<Note> noteSet){this.noteSet = noteSet;}

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Set<Note> getNoteSet() {return noteSet;}
}
