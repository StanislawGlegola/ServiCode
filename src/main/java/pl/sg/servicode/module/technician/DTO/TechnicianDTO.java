package pl.sg.servicode.module.technician.DTO;

public class TechnicianDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String fGaz;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFGaz() {
        return fGaz;
    }

    public void setFGaz(String fGaz) {
        this.fGaz = fGaz;
    }
} 