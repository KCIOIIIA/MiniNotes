package com.example.mininotes.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Indexed;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Indexed
@Table(name = "PS_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name")
    @NotNull
    private String name;
    @Column(name="password")
    @NotNull
    private String password;
    @Column(name="role")
    private Role role;
    @Column(name="status")
    private Status status;
    @Column(name="countFolders")
    private int countFolders;

    @Column(name="countNotes")
    private int countNotes;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Folder> folderSet = new HashSet<>();

    public User() {}

    public void addFolder(Folder folder){
        folderSet.add(folder);
        folder.setUser(this);
    }
    public void removeFolder(Folder folder){
        folderSet.remove(folder);
        folder.setUser(null);
    }

    public User(long id, String name, String password, Role role,
                Status status){
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(Role role) {this.role = role;}
    public void setStatus(Status status) {this.status = status;}
    public void setCountFolders(int countFolders) {this.countFolders = countFolders;}
    public void setCountNotes(int countNotes) {this.countNotes = countNotes;}
    public void setFolderSet(Set<Folder> folderSet){this.folderSet = folderSet;}

    public long getId() {return id;}
    public String getName() {return name;}
    public String getPassword() {return password;}

    public Role getRole() {return role;}
    public Status getStatus() {return status;}
    public int getCountFolders() {return countFolders;}
    public int getCountNotes() {return countNotes;}

    public Set<Folder> getFolderSet() {return folderSet;}

}

