package models;

import java.io.File;

public class GameSettings {

    private File file;
    private int style = 0;

    public GameSettings() {}

    public GameSettings(File file, int style) {
        this.file = file;
        this.style = style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public int getStyle() {
        return style;
    }
}
