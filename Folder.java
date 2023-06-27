import java.util.ArrayList;
import java.util.List;

public class Folder {
    public long id;
    public String name;
    public List<Note> noteList;

    public Folder(){
        noteList = new ArrayList<>();
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public List<Note> getNoteList() {
        return noteList;
    }
}
