package com.example.mininotes.controller;

import com.example.mininotes.models.Folder;
import com.example.mininotes.models.Note;
import com.example.mininotes.models.User;
import com.example.mininotes.repository.FolderRep;
import com.example.mininotes.repository.NoteRep;
import com.example.mininotes.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class MiniNotesController {

    /*
@Autowired
    UserRep userRep;

@PostMapping("/add")
    public User add(@RequestBody User user){
    System.out.println(user.getId() + "   " + user.getName());
    userRep.save(user);
    return user;
}
*/
    @Autowired
    NoteRep noteRep;

    @GetMapping("/getNote")
    public List<Note> getNote(){
        return noteRep.findAll();
    }
    @PostMapping("/addNote")
    public Note addNote(@RequestBody Note note){
        System.out.println(note.getId() + "   " + note.getTitle() + "   " + note.getBody() + "   " +
        note.getCreateDateTime() + "   " + note.getUpdateDateTime() + "   " + note.getAccessMode());
        noteRep.save(note);
        return note;
    }

    @Autowired
    FolderRep folderRep;

    @GetMapping("/getFolder")
    public List<Folder> getFolder(){
        return folderRep.findAll();
    }
    @PostMapping("/addFolder")
    public Folder Folder(@RequestBody Folder folder){
        System.out.println(folder.getId() + "   " + folder.getName() + "   " + folder.getNoteSet());
        folderRep.save(folder);
        return folder;
    }

    /*
    @Autowired
    UserRep userRep;

    @GetMapping("/getUser")
    public List<User> getUser(){
        return userRep.findAll();
    }
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        System.out.println(user.getId() + "   " + user.getName() + "   " + user.getPassword() + "   " +
                user.getRole() + "   " + user.getStatus() + "   " + user.getFolderSet());
        userRep.save(user);
        return user;
    }
     */

}
