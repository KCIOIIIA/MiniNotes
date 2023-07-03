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

@Controller
public class MiniNotesController {
    //ОБЩИЕ

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
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIV);
        model.addAttribute("user", user);
        model.addAttribute("password0", password0);
        if (!password.equals(password0)){
            return "registration_error";
        } else {
            //Создать для каждого пользователя начальный проект и заметку
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

    @Autowired
    UserRep userRep;
    @Autowired
    FolderRep folderRep;
    @Autowired
    NoteRep noteRep;
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
        model.addAttribute("id0", id0);
        return "bin";
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

    @GetMapping("/user/{id0}/folder/{id1}/delete")
    public String deleteFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            folder.setIsDelete(true);
            //user.removeFolder(folder);
            //userRep.save(user);
            folderRep.save(folder);
        }
        return "success";
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

            folder.addNote(note);
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

 //   @GetMapping("/user/{id0}/bin")
  //  public String geBinProfile(@PathVariable long id0, Model model) {
 //       return "bin";
 //   }
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
