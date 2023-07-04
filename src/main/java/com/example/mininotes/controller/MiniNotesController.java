package com.example.mininotes.controller;

import com.example.mininotes.models.*;
import com.example.mininotes.repository.FolderRep;
import com.example.mininotes.repository.NoteRep;
import com.example.mininotes.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;


import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MiniNotesController {
    //ОБЩИЕ
    @Autowired
    UserRep userRep;
    @Autowired
    FolderRep folderRep;
    @Autowired
    NoteRep noteRep;

    /*
    @GetMapping(value = "/")
    public String index(Principal principal, Model model) {
        if (principal == null) {
            return "index";
        } else {
//            model.addAttribute("username", principal.getName());
//            model.addAttribute("user", userRepository.getUserByUsername(principal.getName()).get());
            return "redirect:/" + principal.getName();
        }
    }

    @GetMapping(value = "/login")
    public String login(Principal principal, Model model) {
        if (principal == null) {
            return "login";
        } else {
            return "redirect:/";
        }
    }

     */

/*
    @PostMapping("/")
    public String indexPost(@RequestParam("uname") String uname,
                            @RequestParam("psw") String psw, Model model){
        User user = new User();
        user.setName(uname);
        user.setPassword(psw);
        model.addAttribute("uname", uname);
        model.addAttribute("psw", psw);
        System.out.println(user.getName() + ' ' + user.getPassword());
        return "index";
    }
 */
@GetMapping("/")
public String index(){
    return "index";
}
    @GetMapping("/login")
    public String login(){
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
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIV);
        user.setCountFolders(1);
        user.setCountNotes(0);
        System.out.println("В регистрации на post");
        model.addAttribute("user", user);
        model.addAttribute("password0", password0);
        if (!password.equals(password0)){
            return "registration_error";
        } else {
            userRep.save(user);
            Optional<User> userOptional = userRep.findById(user.getId());
            User user1 = userOptional.get();
            Folder folder = new Folder();
            folder.setName("Первый проект");
            folder.setIsDelete(false);
            user1.addFolder(folder);
            userRep.save(user1);
            return "registration";
        }
    }
    @GetMapping("/admin/users")
    public String getUser(Model model){
        model.addAttribute("users", userRep.findAll());
        return "admin_users";
    }

    @GetMapping("/admin/{id0}/role")
    public String getUserRole(@PathVariable long id0, Model model){
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        if (user.getRole().equals(Role.USER)){
            user.setRole(Role.ADMIN);}
        else {
            user.setRole(Role.USER);
        }
        userRep.save(user);
        return "admin_role";
    }
    @GetMapping("/admin/{id0}/status")
    public String getUsertatus(@PathVariable long id0, Model model){
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        if (user.getStatus().equals(Status.ACTIV)){
            user.setStatus(Status.BLOCKED);}
        else {
            user.setStatus(Status.ACTIV);
        }
        userRep.save(user);
        return "admin_role";
    }

    @GetMapping("/user/{id0}/desktop")
    public String getAlbum(@PathVariable long id0, Model model) {
        Optional<User> user = userRep.findById(id0);
        model.addAttribute("folder", user.get().getFolderSet());
        model.addAttribute("id0", id0);
        return "desktop";
    }

    @PostMapping("/user/{id0}/desktop")
    public String postFolder(@PathVariable long id0, @RequestParam("name") String name, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Folder folder = new Folder();
            folder.setName(name);
            folder.setIsDelete(false);
            int k = user.getCountFolders();
            k++;
            user.setCountFolders(k);
            model.addAttribute("name", name);
            model.addAttribute("folder", userOptional.get().getFolderSet());
            model.addAttribute("id0", id0);
            user.addFolder(folder);
            userRep.save(user);
        }
        return "desktop";
    }


    @GetMapping("/user/{id0}/bin")
    public String getBin(@PathVariable long id0, Model model) {
        Optional<User> user = userRep.findById(id0);
        Set<Folder> folders = user.get().getFolderSet();
        //Найти все проекты, у которых поле isDelete = true;
        //Найти все заметки, у которых поле isDelete = true;
        model.addAttribute("folder", user.get().getFolderSet());
       // for(Folder folder: folders){
           // Optional<Folder> f = folderRep.findById(folder.getId());
            //Set<Note> notes = f.get().getNoteSet();
           // model.addAttribute("id1", f.get().getId());
           // model.addAttribute("note", f.get().getNoteSet());
      //  }
        model.addAttribute("id0", id0);
        return "bin";
    }

    @GetMapping("/user/{id0}/bin/delete")
    public String getBinDelete(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Set<Folder> folderSet = user.getFolderSet().stream().
                    filter(folder -> folder.getIsDelete()).collect(Collectors.toSet());
            for(Folder folder: folderSet){
                        folder.setUser(null);
            }
            folderSet.clear();
            userRep.save(user);
        }
        return "redirect:/user/" + id0 + "/bin";
    }


    @GetMapping("/user/{id0}/folder/{id1}/edit")
    public String editFolderGet(@PathVariable long id0, @PathVariable long id1, Model model) {
        return "notesEdit";
    }

    @PostMapping("/user/{id0}/folder/{id1}/edit")
    public String editFolder(@PathVariable long id0, @PathVariable long id1,
                             @RequestParam("name") String name, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            folder.setName(name);
            //model.addAttribute("folder", folder);
            model.addAttribute("name", name);
            model.addAttribute("id0", id0);
            model.addAttribute("id1", id1);
            System.out.println(folder.getName());
            folderRep.save(folder);
        }
        return "notes";
    }

    @GetMapping("/user/{id0}/folder/{id1}/note/{id2}")
    public String editNoteGet(@PathVariable long id0, @PathVariable long id1,
                              @PathVariable long id2, Model model) {
        Optional<Note> noteOptional = noteRep.findById(id2);
        Note note = noteOptional.get();
        model.addAttribute("id0", id0);
        model.addAttribute("id1", id1);
        model.addAttribute("id2", id2);
        model.addAttribute("title", note.getTitle());
        model.addAttribute("contentbox", note.getBody());
        model.addAttribute("updateDateTime", note.getUpdateDateTime());
        return "editor";
    }
    @PostMapping("/user/{id0}/folder/{id1}/note/{id2}")
    public String editNotePost(@PathVariable long id0, @PathVariable long id1,
                               @PathVariable long id2,
                               @RequestParam("title") String title,
                               @RequestParam("contentbox") String body,
                               Model model) {
        Optional<Note> noteOptional = noteRep.findById(id2);
        Note note = noteOptional.get();
        note.setTitle(title);
        note.setBody(body);
        model.addAttribute("id0", id0);
        model.addAttribute("id1", id1);
        model.addAttribute("id2", id2);
        model.addAttribute("title", note.getTitle());
        model.addAttribute("contentbox", note.getBody());
        String date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
        note.setUpdateDateTime(date);
        noteRep.save(note);
        return "editor";
    }

    @GetMapping("/user/{id0}/folder/{id1}/note/{id2}/delete")
    public String deleteNoteGet(@PathVariable long id0, @PathVariable long id1,
                              @PathVariable long id2, Model model) {
        Optional<Note> noteOptional = noteRep.findById(id2);
        Optional<User> userOptional = userRep.findById(id0);
        Note note = noteOptional.get();
        note.setIsDelete(true);
        int k = userOptional.get().getCountNotes();
        k--;
        userOptional.get().setCountNotes(k);
        noteRep.save(note);
        return "notes";
    }

    @GetMapping("/user/{id0}/folder/{id1}/delete")
    public String deleteFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            folder.setIsDelete(true);
            int k = user.getCountFolders();
            k--;
            user.setCountFolders(k);
            int n = user.getCountNotes();
            n = n - folder.getNoteSet().size();
            user.setCountNotes(n);
            folderRep.save(folder);
        }
        return "success";
    }

    @GetMapping("/user/{id0}/folder/{id1}/bin/delete")
    public String deleteBinFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            //folder.setIsDelete(true);
            user.removeFolder(folder);
            userRep.save(user);
            folderRep.save(folder);
        }
        return "success0";
    }
    @GetMapping("/user/{id0}/folder/{id1}/bin/edit")
    public String editBinFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            folder.setIsDelete(false);
            int k = user.getCountFolders();
            k++;
            user.setCountFolders(k);
            int n = user.getCountNotes();
            n = n + folder.getNoteSet().size();
            user.setCountNotes(n);
            userRep.save(user);
            folderRep.save(folder);
        }
        return "success1";
    }


    @PostMapping("/user/{id0}/folder/{id1}/notes")
    public String postNotes(@PathVariable long id0, @PathVariable long id1,
                            @RequestParam("name") String name, Model model) {
        Optional<Folder> folderOptional = folderRep.findById(id1);
        Folder folder = folderOptional.get();
        folder.setName(name);
        model.addAttribute("name", name);
        model.addAttribute("id0", id0);
        model.addAttribute("id1", id1);
        model.addAttribute("note", folderOptional.get().getNoteSet());
        folderRep.save(folder);
        return "notes";
    }

    @GetMapping("/user/{id0}/folder/{id1}/notes")
    public String getNotes(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<Folder> folderOptional = folderRep.findById(id1);
        model.addAttribute("note", folderOptional.get().getNoteSet());
        model.addAttribute("id0", id0);
        model.addAttribute("id1", id1);
        return "notes";
    }
    @GetMapping("/user/{id0}/folder/{id1}/notes/add")
    public String crNotes(@PathVariable long id0, @PathVariable long id1) {
        return "notesAdd";
    }
    @PostMapping("/user/{id0}/folder/{id1}/notes/add")
    public String addNotes(@PathVariable long id0, @PathVariable long id1,
                           @RequestParam("title") String title,
                           @RequestParam("contentbox") String body, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        Optional<Folder> folderOptional = folderRep.findById(id1);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Folder folder = folderOptional.get();
            Note note = new Note();
            note.setTitle(title);
            note.setBody(body);
            note.setIsDelete(false);
            String date = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
            note.setCreateDateTime(date);
            note.setUpdateDateTime(date);
            folder.addNote(note);
            int k = user.getCountNotes();
            k++;
            user.setCountNotes(k);
            user.addFolder(folder);
            folderRep.save(folder);
            userRep.save(user);
        }
        return "notesAdd";
    }



    @PostMapping("/user/{id0}/profile")
    public String profilePost(@PathVariable long id0,
                              @RequestParam("name") String name,
                              Model model){
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        user.setName(name);
        model.addAttribute("name", name);
        System.out.println(user.getName() + ' ' + user.getPassword());
        userRep.save(user);
        return "profile";
    }

    @GetMapping("/user/{id0}/profile")
    public String getProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        model.addAttribute("id0", id0);
        model.addAttribute("name", user.getName());
        model.addAttribute("countFolders", user.getCountFolders());
        model.addAttribute("countNotes", user.getCountNotes());
        return "profile";
    }


    @GetMapping("/user/{id0}/profile/delete")
    public String deleteProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        userRep.delete(user);
        return "index";
    }

    @GetMapping("/user/{id0}/profile/edit")
    public String getEditProfile(@PathVariable long id0, Model model) {
        return "profileEdit";
    }
    @PostMapping("/user/{id0}/profile/edit")
    public String editProfile(@PathVariable long id0,
                              @RequestParam("passwordOld") String passwordOld,
                              @RequestParam("password0") String password0,
                              @RequestParam("password1") String password1,
                              Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        System.out.println("Пароль: "+user.getPassword());

        model.addAttribute("id0", id0);
        model.addAttribute("passwordOld", passwordOld);
        model.addAttribute("password0", password0);
        model.addAttribute("password1", password1);

        if (!passwordOld.equals(user.getPassword())){
            return "editError0";
        } else {
            if (!password0.equals(password1)){
                return "editError1";
            }
            else{
                if (password0.isEmpty()){
                    return "editError3";
                } else {
                    user.setPassword(password0);
                    userRep.save(user);
                    return "profileEditSec";
                }
            }
        }
    }
}
