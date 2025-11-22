package pl.sg.servicode.module.device.mapper;

import org.springframework.stereotype.Component;
import pl.sg.servicode.module.customer.service.CustomerService;
import pl.sg.servicode.module.device.DTO.DeviceDTO;
import pl.sg.servicode.module.device.entity.DeviceEntity;

@Component
public class DeviceMapper {
    private final CustomerService customerService;

    public DeviceMapper(CustomerService customerService) {
        this.customerService = customerService;
    }

    public DeviceDTO toDTO(DeviceEntity entity) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setManufacturer(entity.getManufacturer());
        dto.setCustomerId(entity.getCustomerId());
        
        // Pobierz i ustaw nazwę klienta
        if (entity.getCustomerId() != null) {
            try {
                dto.setCustomerName(customerService.getCustomerById(entity.getCustomerId()).getName());
            } catch (Exception e) {
                dto.setCustomerName("Nieznany klient");
            }
        }
        
        return dto;
    }

    public DeviceEntity toEntity(DeviceDTO dto) {
        DeviceEntity entity = new DeviceEntity();
        entity.setId(dto.getId());
        entity.setModel(dto.getModel());
        entity.setSerialNumber(dto.getSerialNumber());
        entity.setManufacturer(dto.getManufacturer());
        entity.setCustomerId(dto.getCustomerId());
        return entity;
    }
}
