package dto;
import java.io.Serializable;

public class Stop implements Serializable {

    private String id;
    private String name;
    private String sequence;
    private String route_id;

    public Stop(String id, String name, String sequence, String route_id) {
        super();
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.route_id = route_id;
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

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getRouteId() {
        return route_id;
    }

    public void setRouteId(String route_id) {
        this.route_id = route_id;
    }
}
