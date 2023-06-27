import java.util.List;
public class Note {
    public long id;
    public String title;
    public List<String> body;
    public String createDateTime;
    public String updateDateTime;
    public AccessMode accessMode;
}

enum AccessMode {
    CLOSED, VIEWING, VIEWING_EDITING
}
