package pl.sg.servicode.module.protocol.mapper;

import org.springframework.stereotype.Component;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;
import pl.sg.servicode.module.protocol.entity.ProtocolEntity;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.service.CustomerService;
import pl.sg.servicode.module.device.DTO.DeviceDTO;
import pl.sg.servicode.module.device.service.DeviceService;
import pl.sg.servicode.module.technician.DTO.TechnicianDTO;
import pl.sg.servicode.module.technician.service.TechnicianService;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProtocolMapper {
    private final CustomerService customerService;
    private final DeviceService deviceService;
    private final TechnicianService technicianService;

    public ProtocolMapper(CustomerService customerService, 
                         DeviceService deviceService,
                         TechnicianService technicianService) {
        this.customerService = customerService;
        this.deviceService = deviceService;
        this.technicianService = technicianService;
    }

    public ProtocolDTO toDTO(ProtocolEntity entity) {
        ProtocolDTO dto = new ProtocolDTO();
        dto.setId(entity.getId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIssueDescription(entity.getIssueDescription());
        dto.setWorkDescription(entity.getWorkDescription());
        dto.setMaterials(entity.getMaterials());
        dto.setNotes(entity.getNotes());
        dto.setCustomerId(entity.getCustomerId());
        dto.setDeviceId(entity.getDeviceId());
        dto.setTechnicianIds(entity.getTechnicianIds());

        // Pobierz i ustaw nazwę klienta
        CustomerDTO customer = customerService.getCustomerById(entity.getCustomerId());
        dto.setCustomerName(customer.getName());

        // Pobierz i ustaw numer seryjny urządzenia
        DeviceDTO device = deviceService.getDeviceById(entity.getDeviceId());
        dto.setDeviceSerialNumber(device.getSerialNumber());

        // Pobierz i ustaw informacje o technikach
        if (entity.getTechnicianIds() != null) {
            List<ProtocolDTO.TechnicianInfo> technicianInfos = entity.getTechnicianIds().stream()
                .map(id -> {
                    TechnicianDTO tech = technicianService.getTechnicianById(id);
                    ProtocolDTO.TechnicianInfo techInfo = new ProtocolDTO.TechnicianInfo();
                    techInfo.setId(tech.getId());
                    techInfo.setFirstName(tech.getFirstName());
                    techInfo.setLastName(tech.getLastName());
                    techInfo.setFGaz(tech.getFGaz());
                    return techInfo;
                })
                .collect(Collectors.toList());
            dto.setTechnicians(technicianInfos);
        }

        return dto;
    }

    public ProtocolEntity toEntity(ProtocolDTO dto) {
        ProtocolEntity entity = new ProtocolEntity();
        entity.setId(dto.getId());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIssueDescription(dto.getIssueDescription());
        entity.setWorkDescription(dto.getWorkDescription());
        entity.setMaterials(dto.getMaterials());
        entity.setNotes(dto.getNotes());
        entity.setCustomerId(dto.getCustomerId());
        entity.setDeviceId(dto.getDeviceId());
        entity.setTechnicianIds(dto.getTechnicianIds());
        return entity;
    }
} 