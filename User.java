import java.util.ArrayList;
import java.util.List;

public class User {
    public long id;
    public String name;
    public String password;
    public Role role;
    public Status status;
    public List<Folder> folderList;
    public List<User> userList;

    public User(){
        folderList = new ArrayList<>();
        userList = new ArrayList<>();
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public Role getRole() {
        return role;
    }
    public Status getStatus() {
        return status;
    }
    public List<Folder> getFolderList() {
        return folderList;
    }
    public List<User> getUserList() {
        return userList;
    }
}

enum Role {
    ADMIN, USER
}
enum Status {
    ACTIV, BLOCKED
}
