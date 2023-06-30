package com.example.mininotes.controller;

import com.example.mininotes.models.*;
import com.example.mininotes.repository.FolderRep;
import com.example.mininotes.repository.NoteRep;
import com.example.mininotes.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class MiniNotesController {
    @GetMapping("/desktop")
    public String desktop(){
        return "desktop";
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/user/profile")
    public String getProfile(Model model) {
        //Optional<User> user = userRep.findById(id0);
        //System.out.println(user.get().getName());
        //Optional<Folder> folder = folderRep.findById(id0);
        //System.out.println(folder.get().getName());
        //model.addAttribute("folder", user.get().getFolderSet());
        //model.addAttribute("id0", id0);
        return "profile";
    }

    @Autowired
    UserRep userRep;
    @Autowired
    FolderRep folderRep;
    @Autowired
    NoteRep noteRep;

    //ГРУППЫ
    @GetMapping("/users")
    public String getUser(Model model){
        model.addAttribute("users", userRep.findAll());
        return "users";
    }
    @GetMapping("/registration")
    public String crUser(){
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(@RequestParam("name") String name, @RequestParam("password") String password, Model model){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        model.addAttribute("user", user);
        System.out.println(user.getId() + "   " + user.getName());
        userRep.save(user);
        return "registration";
    }
    @GetMapping("/user/{id0}/desktop")
    public String getAlbum(@PathVariable long id0, Model model) {
        Optional<User> user = userRep.findById(id0);
        //System.out.println(user.get().getName());
        //Optional<Folder> folder = folderRep.findById(id0);
        //System.out.println(folder.get().getName());
        //model.addAttribute("folder", user.get().getFolderSet());
        model.addAttribute("id0", id0);
        return "desktop";
    }
    @GetMapping("/user/{id0}/profile")
    public String getProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        model.addAttribute("id0", id0);
        model.addAttribute("user", user);
        return "profile";
    }
/*
    @GetMapping("/groups/{id0}/albums/add")
    public String crAlbum(@PathVariable long id0) {
        return "albumAdd";
    }
    @PostMapping("/groups/{id0}/albums/add")
    public String addAlbum(@PathVariable long id0, @RequestParam("name") String name,
                           @RequestParam("years") String years, Model model) {
        Optional<Group> groupOptional = groupRepository.findById(id0);
        System.out.println(name + " " + years);
        if (groupOptional.isPresent()){
            Group group = groupOptional.get();
            Album album = new Album();
            album.setName(name);
            album.setYears(years);
            group.addAlbum(album);
            groupRepository.save(group);
        }
        return "albumAdd";
    }

 */


}
