package DTO;

/**
 * Created by iTai on 10/14/2017.
 */

public class Schedule {
    private String id;
    private String Name;

    public Schedule(){

    }

    public Schedule(String name) {
        id="0";
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
