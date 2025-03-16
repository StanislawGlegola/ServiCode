package pl.sg.servicode.module.device.mapper;

import org.springframework.stereotype.Component;
import pl.sg.servicode.module.device.DTO.DeviceDTO;
import pl.sg.servicode.module.device.entity.DeviceEntity;

@Component
public class DeviceMapper {
    public DeviceDTO toDTO(DeviceEntity entity) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(entity.getId());
        dto.setModel(entity.getModel());
        dto.setSerialNumber(entity.getSerialNumber());
        dto.setManufacturer(entity.getManufacturer());
        dto.setCustomerId(entity.getCustomerId());
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
