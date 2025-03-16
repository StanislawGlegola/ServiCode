package pl.sg.servicode.module.protocol.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "protocol")
public class ProtocolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "issue_description")
    private String issueDescription;

    @Column(name = "work_description")
    private String workDescription;

    @Column(name = "materials")
    private String materials;

    @Column(name = "notes")
    private String notes;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @ElementCollection
    @CollectionTable(name = "protocol_technician", joinColumns = @JoinColumn(name = "protocol_id"))
    @Column(name = "technician_id")
    private List<Integer> technicianIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public List<Integer> getTechnicianIds() {
        return technicianIds;
    }

    public void setTechnicianIds(List<Integer> technicianIds) {
        this.technicianIds = technicianIds;
    }
} 