package com.example.mininotes.controller;

import com.example.mininotes.models.*;
import com.example.mininotes.repository.FolderRep;
import com.example.mininotes.repository.NoteRep;
import com.example.mininotes.repository.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
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
    /**Общаяя для всех пользователей страница с переходом на вход/регистрацию**/
    @GetMapping("/")
    public String index(){
        return "index";
    }
    /**Общаяя для всех пользователей страница входа**/
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
    /**Общаяя для всех пользователей страница регистрации**/
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
    /**Страница для администратора со списоком всех пользователей**/
    @GetMapping("/admin/users")
    public String getUser(Model model){
        model.addAttribute("users", userRep.findAll());
        return "admin_users";
    }
    /**Страница для администратора со сменой роли пользователя**/
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
    /**Страница для администратора со сменой статуса пользователя**/
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
    /**Рабочий стол пользователя**/
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
    /**Заметки в проекте пользователя**/
    @GetMapping("/user/{id0}/folder/{id1}/notes")
    public String getNotes(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<Folder> folderOptional = folderRep.findById(id1);
        model.addAttribute("note", folderOptional.get().getNoteSet());
        model.addAttribute("id0", id0);
        model.addAttribute("id1", id1);
        return "notes";
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
    /**Удаление (в корзину) проекта**/
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
            Set<Note> notes = folder.getNoteSet();
            for(Note note : notes){
                if (!note.getIsDelete()){
                    n--;
                }
                note.setIsDelete(true);
                System.out.println("Заметка "+note.getTitle()+" в корзине");
                noteRep.save(note);
            }
            user.setCountNotes(n);
            folderRep.save(folder);
        }
        return "success";
    }
    /**Добавление заметки**/
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
    /**Редактирование/удаление (в корзину) заметки**/
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
    /**Удаление (в корзину) заметки**/
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
    /**Корзина**/
    @GetMapping("/user/{id0}/bin")
    public String getBin(@PathVariable long id0, Model model) {
        Optional<User> user = userRep.findById(id0);
        Set<Folder> folders = user.get().getFolderSet();
        model.addAttribute("folder", folders);
        for(Folder folder: folders){
            Set <Note> notes = folder.noteSet;
            if (!folder.getIsDelete()) {
                System.out.println("Проект "+ folder.getName()+" не в корзине");
                for(Note note: notes) {
                    if (note.getIsDelete()) {
                        System.out.println("Заметка " + note.getTitle() + " в корзине");
                        model.addAttribute("notes", notes);
                        model.addAttribute("id1", folder.getId());
                    } else {
                        System.out.println("Заметка " + note.getTitle() + " не в корзине");
                    }
                }
            }
        }
        model.addAttribute("id0", id0);
        return "bin";
    }
    /**Очистить корзину**/
    @GetMapping("/user/{id0}/bin/delete")
    public String getBinDelete(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Set<Folder> folderSet = user.getFolderSet().stream().
                    filter(folder -> folder.getIsDelete()).collect(Collectors.toSet());
            Set<Folder> folders = user.getFolderSet();
            for(Folder folder: folders){
                Set <Note> notes = folder.getNoteSet();
                if (!folder.getIsDelete()){
                    for(Note note: notes){
                        if (note.getIsDelete()){
                            System.out.println("Заметка "+note.getTitle()+" полностью удалена, хотя проект нет");
                            note.setFolder(null);
                        }

                    }
                    notes.clear();
                }
            }
            for(Folder folder: folderSet){
                folder.setUser(null);
            }
            folderSet.clear();
            userRep.save(user);
        }
        return "redirect:/user/" + id0 + "/bin";
    }
    /**Удалить проект в корзине**/
    @GetMapping("/user/{id0}/folder/{id1}/bin/delete")
    public String deleteBinFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            //folder.setIsDelete(true);
            if (folder.getIsDelete()){
                user.removeFolder(folder);}
            else{
                Set<Note> notes = folder.getNoteSet();
                for(Note note: notes){
                    if (note.getIsDelete()) {
                        folder.removeNote(note);
                    }
                }
            }
            userRep.save(user);
            folderRep.save(folder);
        }
        return "success0";
    }
    /**Восстановить проект из корзины**/
    @GetMapping("/user/{id0}/folder/{id1}/bin/edit")
    public String editBinFolder(@PathVariable long id0, @PathVariable long id1, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Folder> folderOptional = folderRep.findById(id1);
            Folder folder = folderOptional.get();
            if (folder.getIsDelete()){
                folder.setIsDelete(false);
                Set<Note> notes = folder.getNoteSet();
                for(Note note: notes){
                    note.setIsDelete(false);
                    System.out.println("Заметка "+note.getTitle()+" восстановлена");
                }
            }
            else{
                System.out.println("Попадаю сюда с именем проекта "+folder.getName());
                return "redirect:/user/" + id0 + "/bin";
            }
            int k = user.getCountFolders();
            k++;
            user.setCountFolders(k);
            int n = user.getCountNotes();
            n = n + folder.getNoteSet().size();
            System.out.println("Кол-во заметок в проекте "+ folder.getName()+" = "+folder.getNoteSet().size());
            user.setCountNotes(n);
            userRep.save(user);
            folderRep.save(folder);
        }
        return "success1";
    }
    /**Восстановить заметку из корзины**/
    @GetMapping("/user/{id0}/folder/{id1}/note/{id2}/bin/edit")
    public String editBinNote(@PathVariable long id0, @PathVariable long id1,
                              @PathVariable long id2, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        Optional<Note> noteOptional = noteRep.findById(id2);
        Note note = noteOptional.get();
        System.out.println("Заметка "+note.getTitle()+" восстановлена!");
        note.setIsDelete(false);
        noteRep.save(note);
        int n = user.getCountNotes();
        n++;
        user.setCountNotes(n);
        userRep.save(user);
        noteRep.save(note);
        return "success1";
    }
    /**Удалить заметку из корзины**/
    @GetMapping("/user/{id0}/folder/{id1}/note/{id2}/bin/delete")
    public String deleteBinNote(@PathVariable long id0, @PathVariable long id1,
                              @PathVariable long id2, Model model) {
        Optional<Note> noteOptional = noteRep.findById(id2);
        Note note = noteOptional.get();
        System.out.println("Заметка "+note.getTitle()+" полностью удалена!");
        note.setFolder(null);
        noteRep.save(note);
        return "success0";
    }
    /**Профиль пользователя**/
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

    /**Удалить профиль пользователя**/
    @GetMapping("/user/{id0}/profile/delete")
    public String deleteProfile(@PathVariable long id0, Model model) {
        Optional<User> userOptional = userRep.findById(id0);
        User user = userOptional.get();
        userRep.delete(user);
        return "index";
    }
    /**Редактировать профиль пользователя**/
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
