package pl.sg.servicode.module.protocol.DTO;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class ProtocolDTO {
    private Integer id;
    
    @NotNull
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String issueDescription;
    private String workDescription;
    private String materials;
    private String notes;
    
    @NotNull
    private Integer customerId;
    private String customerName;
    
    @NotNull
    private Integer deviceId;
    private String deviceSerialNumber;
    
    @NotNull
    private List<Integer> technicianIds;
    private List<TechnicianInfo> technicians = new ArrayList<>();

    // Klasa wewnętrzna dla informacji o techniku
    public static class TechnicianInfo {
        private Integer id;
        private String firstName;
        private String lastName;
        private String fGaz;

        // Gettery i settery
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

    // Gettery i settery
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public List<Integer> getTechnicianIds() {
        return technicianIds;
    }

    public void setTechnicianIds(List<Integer> technicianIds) {
        this.technicianIds = technicianIds;
    }

    public List<TechnicianInfo> getTechnicians() {
        return technicians;
    }

    public void setTechnicians(List<TechnicianInfo> technicians) {
        this.technicians = technicians;
    }
} 