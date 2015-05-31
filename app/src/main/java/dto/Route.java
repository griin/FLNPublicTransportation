package dto;
import java.io.Serializable;

public class Route implements Serializable {

    private String id;
    private String short_name;
    private String long_name;
    private String last_modified_date;
    private String agency_id;

    public Route(String id, String short_name, String long_name,
                 String last_modified_date, String agency_id) {
        super();
        this.id = id;
        this.short_name = short_name;
        this.long_name = long_name;
        this.last_modified_date = last_modified_date;
        this.agency_id = agency_id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
    }

    public String getLongName() {
        return long_name;
    }

    public void setLongName(String long_name) {
        this.long_name = long_name;
    }

    public String getLastModifiedDate() {
        return last_modified_date;
    }

    public void setLastModifiedDate(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getAgencyId() {
        return agency_id;
    }

    public void setAgencyId(String agency_id) {
        this.short_name = agency_id;
    }
}
