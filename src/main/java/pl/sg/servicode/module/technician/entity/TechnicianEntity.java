package pl.sg.servicode.module.technician.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "technician")
public class TechnicianEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "f_gaz", nullable = false, unique = true)
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