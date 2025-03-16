package pl.sg.servicode.module.customer.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.customer.DTO.CustomerDTO;
import pl.sg.servicode.module.customer.service.CustomerService;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String showCustomerList(Model model) {
        List<CustomerDTO> customers = customerService.getCustomerList();
        model.addAttribute("customers", customers);
        model.addAttribute("customer", new CustomerDTO());
        model.addAttribute("activeTab", "customer");
        return "customers";
    }

    @PostMapping("/save")
    public String saveCustomer(@ModelAttribute @Valid CustomerDTO customerDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/customer";
        }

        if (customerDTO.getId() == null) {
            customerService.saveCustomer(customerDTO); // Nowy klient
            redirectAttributes.addFlashAttribute("message", "Nowy klient dodany!");
        } else {
            customerService.editCustomer(customerDTO); // Edycja istniejącego klienta
            redirectAttributes.addFlashAttribute("message", "Dane klienta zaktualizowane!");
        }
        return "redirect:/customer";
    }


    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable int id, RedirectAttributes redirectAttributes) {
        customerService.deleteCustomer(id);
        redirectAttributes.addFlashAttribute("message", "Klient został usunięty pomyślnie!");
        return "redirect:/customer";
    }
}