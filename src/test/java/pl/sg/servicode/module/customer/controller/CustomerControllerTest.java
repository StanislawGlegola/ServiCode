package pl.sg.servicode.module.customer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.service.CustomerService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Konfiguracja dla testowania kontrolera Spring MVC
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setViewResolvers(viewResolver)
                .build();

        // Przykładowe dane
        customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("Test Company");
        customerDTO.setCity("Warszawa");
        customerDTO.setPostalCode("00-001");
        customerDTO.setStreet("Testowa 123");
    }

    @Test
    void showCustomerListShouldDisplayCustomersView() throws Exception {
        // given
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.getCustomerList()).thenReturn(customers);

        // when/then
        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attribute("customers", customers));

        verify(customerService, times(1)).getCustomerList();
    }

    @Test
    void saveCustomerShouldCreateNewCustomerAndRedirect() throws Exception {
        // given
        doNothing().when(customerService).saveCustomer(any(CustomerDTO.class));

        // when/then
        mockMvc.perform(post("/customer/save")
                        .param("id", "0")
                        .param("name", "New Company")
                        .param("city", "Gdańsk")
                        .param("postalCode", "80-001")
                        .param("street", "Morska 789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService, times(1)).saveCustomer(any(CustomerDTO.class));
    }

    @Test
    void saveCustomerShouldUpdateExistingCustomerAndRedirect() throws Exception {
        // given
        doNothing().when(customerService).editCustomer(any(CustomerDTO.class));

        // when/then
        mockMvc.perform(post("/customer/save")
                        .param("id", "1")
                        .param("name", "Updated Company")
                        .param("city", "Kraków")
                        .param("postalCode", "30-001")
                        .param("street", "Nowa 456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService, times(1)).editCustomer(any(CustomerDTO.class));
    }

    @Test
    void deleteCustomerShouldRemoveCustomerAndRedirect() throws Exception {
        // given
        doNothing().when(customerService).deleteCustomer(1);

        // when/then
        mockMvc.perform(get("/customer/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/customer"))
                .andExpect(flash().attributeExists("message"));

        verify(customerService, times(1)).deleteCustomer(1);
    }
}