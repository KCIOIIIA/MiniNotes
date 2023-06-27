import java.util.ArrayList;
import java.util.List;
public class Note {
    public long id;
    public String title;
    public List<String> body;
    public String createDateTime;
    public String updateDateTime;
    public AccessMode accessMode;

    public Note(){
        body = new ArrayList<>();
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(List<String> body) {
        this.body = body;
    }
    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }
    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
    public void setAccessMode(AccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public List<String> getBody() {
        return body;
    }
    public String getCreateDateTime() {
        return createDateTime;
    }
    public String getUpdateDateTime() {
        return updateDateTime;
    }
    public AccessMode getAccessMode() {
        return accessMode;
    }
}

enum AccessMode {
    CLOSED, VIEWING, VIEWING_EDITING
}
