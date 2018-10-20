package wannabit.io.eoswallet.model;

public class WBLanguage {
    int id;
    String name;
    boolean checked;

    public WBLanguage() {
    }

    public WBLanguage(int id, String name, boolean checked) {
        this.id = id;
        this.name = name;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
