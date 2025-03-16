package pl.sg.servicode.module.customer.mapper;

import org.junit.jupiter.api.Test;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.entity.CustomerEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerMapperTest {

    private CustomerMapper customerMapper = new CustomerMapper();

    @Test
    void shouldMapEntityToDTO() {
        // given
        CustomerEntity entity = new CustomerEntity();
        entity.setId(1);
        entity.setName("Test Company");
        entity.setCity("Warszawa");
        entity.setPostalCode("00-001");
        entity.setStreet("Testowa 123");

        // when
        CustomerDTO dto = customerMapper.toDTO(entity);

        // then
        assertEquals(1, dto.getId());
        assertEquals("Test Company", dto.getName());
        assertEquals("Warszawa", dto.getCity());
        assertEquals("00-001", dto.getPostalCode());
        assertEquals("Testowa 123", dto.getStreet());
    }

    @Test
    void shouldMapDTOToEntity() {
        // given
        CustomerDTO dto = new CustomerDTO();
        dto.setId(1);
        dto.setName("Test Company");
        dto.setCity("Warszawa");
        dto.setPostalCode("00-001");
        dto.setStreet("Testowa 123");

        // when
        CustomerEntity entity = customerMapper.toEntity(dto);

        // then
        assertEquals(1, entity.getId());
        assertEquals("Test Company", entity.getName());
        assertEquals("Warszawa", entity.getCity());
        assertEquals("00-001", entity.getPostalCode());
        assertEquals("Testowa 123", entity.getStreet());
    }

    @Test
    void shouldHandleNullEntityToDTO() {
        // when
        CustomerDTO dto = customerMapper.toDTO(null);

        // then
        assertNull(dto);
    }

    @Test
    void shouldHandleNullDTOToEntity() {
        // when
        CustomerEntity entity = customerMapper.toEntity(null);

        // then
        assertNull(entity);
    }
}