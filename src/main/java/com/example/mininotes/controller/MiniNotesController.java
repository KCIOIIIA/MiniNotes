package com.example.mininotes.controller;

import com.example.mininotes.models.*;
import com.example.mininotes.repository.FolderRep;
import com.example.mininotes.repository.NoteRep;
import com.example.mininotes.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MiniNotesController {
    //ОБЩИЕ
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @PostMapping("/login")
    public String postlogin(Model model){
        return "login";
    }
    @GetMapping("/registration")
    public String crUser(){
        return "registration";
    }
    @PostMapping("/registration")
    public String createUser(@RequestParam("name") String name,
                             @RequestParam("password") String password,
                             @RequestParam("password0") String password0, Model model){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        model.addAttribute("user", user);
        model.addAttribute("password0", password0);
        if (!password.equals(password0)){
            return "registration_error";
        } else {
            System.out.println(user.getId() + "   " + user.getName());
            userRep.save(user);
            return "registration";
        }
    }
    @GetMapping("/admin/users")
    public String getUser(Model model){
        model.addAttribute("users", userRep.findAll());
        return "admin_users";
    }

    @Autowired
    UserRep userRep;
    @Autowired
    FolderRep folderRep;
    @Autowired
    NoteRep noteRep;
    @GetMapping("/user/{id0}/desktop")
    public String getAlbum(@PathVariable long id0, Model model) {
        Optional<User> user = userRep.findById(id0);
        Optional<Folder> folder = folderRep.findById(id0);
        model.addAttribute("folder", user.get().getFolderSet());
        model.addAttribute("id0", id0);
        return "desktop";
    }
    @GetMapping("/user/{id0}/folder/add")
    public String getFolder(@PathVariable long id0) {
        return "folderAdd";
    }
    @PostMapping("/user/{id0}/folder/add")
    public String postFolder(@PathVariable long id0, @RequestParam("name") String name, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Folder folder = new Folder();
            folder.setName(name);
            user.addFolder(folder);
            userRep.save(user);
        }
        return "folderAdd";
    }
    @GetMapping("/user/{id0}/profile")
    public String getProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        model.addAttribute("id0", id0);
        model.addAttribute("user", user);
        return "profile";
    }
    @GetMapping("/user/{id0}/profile/delete")
    public String deleteProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        userRep.delete(user);
        return "index";
    }

    //ПРОБЛЕМЫ

    @GetMapping("/user/{id0}/profile/edit")
    public String getEditProfile(@PathVariable long id0, Model model) {
        return "profileEdit";
    }
    @PostMapping("/user/{id0}/profile/edit")
    public String editProfile(@PathVariable long id0,
                              //@RequestParam("passwordOld") String passwordOld,
                              @RequestParam("name") String name,
                              @RequestParam("password") String password,
                              Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        //User userOp = userOptional.get();
        //User user = new User();
        User user = userOptional.get();
        user.setName(name);
        user.setPassword(password);
        //model.addAttribute("passwordOld", userOp.getPassword());
        model.addAttribute("user", user);
        model.addAttribute("id0", id0);
        userRep.save(user);
        return "profileEdit";
    }

    @GetMapping("/user/{id0}/bin")
    public String geBinProfile(@PathVariable long id0, Model model) {
        return "bin";
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
