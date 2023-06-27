import java.util.List;

public class User {
    public long id;
    public String name;
    public String password;
    public Role role;
    public Status status;
    public List<Folder> folderList;
    public List<User> userList;
}

enum Role {
    ADMIN, USER
}
enum Status {
    ACTIV, BLOCKED
}
