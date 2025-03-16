package pl.sg.servicode.module.customer.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import pl.sg.servicode.module.customer.entity.CustomerEntity;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.mapper.CustomerMapper;
import pl.sg.servicode.module.customer.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getCustomerList() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(int id) {
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Klient o ID " + id + " nie został znaleziony"));
        return customerMapper.toDTO(customerEntity);
    }


    public void deleteCustomer(int id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Klient o ID " + id + " nie został znaleziony");
        }
        customerRepository.deleteById(id);
    }


    public void saveCustomer(@Valid CustomerDTO customerDTO) {
        CustomerEntity entity = customerMapper.toEntity(customerDTO);
        customerRepository.save(entity);
    }

    public void editCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new RuntimeException("Klient o ID " + customerDTO.getId() + " nie został znaleziony"));

        // Aktualizacja danych
        customerEntity.setName(customerDTO.getName());
        customerEntity.setCity(customerDTO.getCity());
        customerEntity.setPostalCode(customerDTO.getPostalCode());
        customerEntity.setStreet(customerDTO.getStreet());

        customerRepository.save(customerEntity);
    }
}
