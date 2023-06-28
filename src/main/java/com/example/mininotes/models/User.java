package com.example.mininotes.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
/*
@Entity
@Table(name = "USER")

 */
public class User {
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="password")
    private String password;
    @Column(name="role")
    private Role role;
    @Column(name="status")
    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Folder> folderSet = new HashSet<>();

    //public List<User> userList;

    public User() {}
    public void setId(long id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(Role role) {this.role = role;}
    public void setStatus(Status status) {this.status = status;}
    public void setFolderSet(Set<Folder> folderSet){this.folderSet = folderSet;}

    //public void setUserList(List<User> userList) {this.userList = userList;}

    public long getId() {return id;}
    public String getName() {return name;}
    public String getPassword() {return password;}
    public Role getRole() {return role;}
    public Status getStatus() {return status;}
    public Set<Folder> getFolderSet() {return folderSet;}

    //public List<User> getUserList() {return userList;}

     */
}

enum Role {
    ADMIN, USER
}

enum Status {
    ACTIV, BLOCKED
}
