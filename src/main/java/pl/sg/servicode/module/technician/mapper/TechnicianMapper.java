package pl.sg.servicode.module.technician.mapper;

import org.springframework.stereotype.Component;
import pl.sg.servicode.module.technician.DTO.TechnicianDTO;
import pl.sg.servicode.module.technician.entity.TechnicianEntity;

@Component
public class TechnicianMapper {
    public TechnicianDTO toDTO(TechnicianEntity entity) {
        TechnicianDTO dto = new TechnicianDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setFGaz(entity.getFGaz());
        return dto;
    }

    public TechnicianEntity toEntity(TechnicianDTO dto) {
        TechnicianEntity entity = new TechnicianEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setFGaz(dto.getFGaz());
        return entity;
    }
} 