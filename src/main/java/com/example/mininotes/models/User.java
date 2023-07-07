package com.example.mininotes.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "PS_USER")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="name")
    private String name;
    @NotNull
    @NotEmpty
    private String username;
    @NotNull
    @NotEmpty
    private String password;
    private UserStatus status;

    private UserRole role;
    private Integer countFolders;
    private Integer countNotes;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Folder> FolderSet = new HashSet<>();
    public User() {}
    public User(String username, String password, UserStatus status, UserRole role) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public void addFolder(Folder Folder) {
        FolderSet.add(Folder);
        Folder.setUser(this);
    }

    public void removeFolder(Folder Folder) {
        FolderSet.remove(Folder);
        Folder.setUser(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public UserStatus getStatus() {
        return status;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    public Set<Folder> getFolderSet() {
        return FolderSet;
    }
    public void setFolderSet(Set<Folder> FolderSet) {
        this.FolderSet = FolderSet;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getCountNotes() {return countNotes;}
    public Integer getCountFolders() {
        return countFolders;
    }
    public void setCountNotes(Integer countNotes) {
        this.countNotes = countNotes;
    }
    public void setCountFolders(Integer countFolders) {
        this.countFolders = countFolders;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
