package cm.uds.iutfv.gi.lir.controleacces.recycler;

import android.graphics.Bitmap;

/**
 * Created by men_franc 18-05-2018
 */

public class Etudiants {

    private String name;
    private String description;
    private String regime;
    private Bitmap avatar;

    public Etudiants(String name, String description, Bitmap avatar) {
        this.name = name;
        this.description = description;
        this.avatar = avatar;
    }

    public Etudiants() {
        this.description = "Regulier";
        this.name = "MENKAM Francis";
        //this.avatar = new BitmapFactory();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegime() {
        return regime;
    }

    public Bitmap getAvatar() {
        return avatar;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}