package dto;
import java.io.Serializable;

public class Departure implements Serializable {

    private String id;
    private String calendar;
    private String time;

    public Departure(String id, String calendar, String time) {
        super();
        this.id = id;
        this.calendar = calendar;
        this.time = time;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
