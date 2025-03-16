package pl.sg.servicode.module.customer.mapper;

import org.springframework.stereotype.Component;
import pl.sg.servicode.module.customer.entity.CustomerEntity;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;

@Component
public class CustomerMapper {
    public CustomerDTO toDTO(CustomerEntity entity) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPostalCode(entity.getPostalCode());
        dto.setCity(entity.getCity());
        dto.setStreet(entity.getStreet());
        return dto;
    }

    public CustomerEntity toEntity(CustomerDTO dto) {
        CustomerEntity entity = new CustomerEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPostalCode(dto.getPostalCode());
        entity.setCity(dto.getCity());
        entity.setStreet(dto.getStreet());
        return entity;
    }
}