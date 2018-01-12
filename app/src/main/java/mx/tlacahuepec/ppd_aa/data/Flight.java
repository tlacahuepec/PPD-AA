package mx.tlacahuepec.ppd_aa.data;

import io.realm.RealmObject;

/**
 * Created by santi on 1/11/2018.
 */

public class Flight extends RealmObject {
    private String id;
    private String name;
    private boolean saved;

    // Required
    public Flight() {
    }

    public Flight(String id, String name, boolean saved) {
        this.id = id;
        this.name = name;
        this.saved = saved;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
