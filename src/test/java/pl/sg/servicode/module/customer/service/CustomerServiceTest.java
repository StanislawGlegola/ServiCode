package pl.sg.servicode.module.customer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.entity.CustomerEntity;
import pl.sg.servicode.module.customer.mapper.CustomerMapper;
import pl.sg.servicode.module.customer.repository.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private CustomerDTO customerDTO;
    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Przykładowe dane dla testów
        customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("Test Company");
        customerDTO.setCity("Warszawa");
        customerDTO.setPostalCode("00-001");
        customerDTO.setStreet("Testowa 123");

        customerEntity = new CustomerEntity();
        customerEntity.setId(1);
        customerEntity.setName("Test Company");
        customerEntity.setCity("Warszawa");
        customerEntity.setPostalCode("00-001");
        customerEntity.setStreet("Testowa 123");

        // Konfiguracja mapowania
        when(customerMapper.toEntity(any(CustomerDTO.class))).thenReturn(customerEntity);
        when(customerMapper.toDTO(any(CustomerEntity.class))).thenReturn(customerDTO);
    }

    @Test
    void getCustomerListShouldReturnAllCustomers() {
        // given
        List<CustomerEntity> entities = Arrays.asList(customerEntity);
        when(customerRepository.findAll()).thenReturn(entities);

        // when
        List<CustomerDTO> result = customerService.getCustomerList();

        // then
        assertEquals(1, result.size());
        assertEquals("Test Company", result.get(0).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getCustomerByIdShouldReturnCustomerWhenExists() {
        // given
        when(customerRepository.findById(1)).thenReturn(Optional.of(customerEntity));

        // when
        CustomerDTO result = customerService.getCustomerById(1);

        // then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Company", result.getName());
        verify(customerRepository, times(1)).findById(1);
    }

    @Test
    void getCustomerByIdShouldThrowExceptionWhenCustomerNotFound() {
        // given
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // when, then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(999);
        });

        assertTrue(exception.getMessage().contains("nie został znaleziony"));
        verify(customerRepository, times(1)).findById(999);
    }

    @Test
    void saveCustomerShouldSaveNewCustomer() {
        // given
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);

        // when
        customerService.saveCustomer(customerDTO);

        // then
        verify(customerMapper, times(1)).toEntity(customerDTO);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void editCustomerShouldUpdateExistingCustomer() {
        // given
        CustomerDTO updatedDTO = new CustomerDTO();
        updatedDTO.setId(1);
        updatedDTO.setName("Updated Company");
        updatedDTO.setCity("Kraków");
        updatedDTO.setPostalCode("30-001");
        updatedDTO.setStreet("Nowa 456");

        CustomerEntity existingEntity = new CustomerEntity();
        existingEntity.setId(1);
        existingEntity.setName("Test Company");
        existingEntity.setCity("Warszawa");
        existingEntity.setPostalCode("00-001");
        existingEntity.setStreet("Testowa 123");

        CustomerEntity updatedEntity = new CustomerEntity();
        updatedEntity.setId(1);
        updatedEntity.setName("Updated Company");
        updatedEntity.setCity("Kraków");
        updatedEntity.setPostalCode("30-001");
        updatedEntity.setStreet("Nowa 456");

        when(customerRepository.findById(1)).thenReturn(Optional.of(existingEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(updatedEntity);

        // when
        customerService.editCustomer(updatedDTO);

        // then
        verify(customerRepository, times(1)).findById(1);
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void editCustomerShouldThrowExceptionWhenCustomerNotFound() {
        // given
        CustomerDTO notFoundDTO = new CustomerDTO();
        notFoundDTO.setId(999);
        when(customerRepository.findById(999)).thenReturn(Optional.empty());

        // when, then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.editCustomer(notFoundDTO);
        });

        assertTrue(exception.getMessage().contains("nie został znaleziony"));
        verify(customerRepository, times(1)).findById(999);
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }



    @Test
    void deleteCustomerShouldThrowExceptionWhenCustomerNotFound() {
        // given
        when(customerRepository.existsById(999)).thenReturn(false);

        // when, then
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(999);
        });

        assertTrue(exception.getMessage().contains("nie został znaleziony"));
        verify(customerRepository, times(1)).existsById(999);
        verify(customerRepository, never()).deleteById(999);
    }
}