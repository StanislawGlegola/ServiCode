package pl.sg.servicode.module.protocol.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;
import pl.sg.servicode.module.protocol.service.ProtocolService;
import pl.sg.servicode.module.customer.service.CustomerService;
import pl.sg.servicode.module.technician.service.TechnicianService;
import pl.sg.servicode.module.device.service.DeviceService;

@Controller
@RequestMapping("/protocol/form")
public class ProtocolFormController {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolFormController.class);

    private final ProtocolService protocolService;
    private final CustomerService customerService;
    private final TechnicianService technicianService;
    private final DeviceService deviceService;

    public ProtocolFormController(ProtocolService protocolService,
                                  CustomerService customerService,
                                  TechnicianService technicianService,
                                  DeviceService deviceService) {
        this.protocolService = protocolService;
        this.customerService = customerService;
        this.technicianService = technicianService;
        this.deviceService = deviceService;
    }

    @GetMapping("/new")
    public String showNewProtocolForm(Model model) {
        model.addAttribute("protocol", new ProtocolDTO());
        model.addAttribute("customers", customerService.getCustomerList());
        model.addAttribute("technicians", technicianService.getTechnicianList());
        model.addAttribute("formMode", "new");
        model.addAttribute("activeTab", "protocol");
        return "protocol/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditProtocolForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProtocolDTO protocol = protocolService.getProtocolById(id);
            if (protocol.getStartDate() != null) {
                // Bez konieczności zmian, jeśli ProtocolDTO zwraca już LocalDateTime
            }
            model.addAttribute("protocol", protocol);
            model.addAttribute("customers", customerService.getCustomerList());
            model.addAttribute("technicians", technicianService.getTechnicianList());
            model.addAttribute("devices", deviceService.getDevicesByCustomerId(protocol.getCustomerId()));
            model.addAttribute("formMode", "edit");
            model.addAttribute("activeTab", "protocol");
            return "protocol/form";
        } catch (Exception e) {
            logger.error("Błąd podczas ładowania protokołu do edycji o ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Nie można załadować protokołu do edycji");
            return "redirect:/protocol";
        }
    }

    @PostMapping("/save")
    public String saveProtocol(@ModelAttribute @Valid ProtocolDTO protocolDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        logger.debug("Próba zapisania protokołu: {}", protocolDTO);

        if (result.hasErrors()) {
            logger.error("Błędy walidacji dla protokołu: {}", result.getAllErrors());
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas zapisywania protokołu: nieprawidłowe dane");
            return "redirect:/protocol/form/new";
        }

        try {
            if (protocolDTO.getId() == null) {
                ProtocolDTO savedProtocol = protocolService.addNewProtocol(protocolDTO);
                logger.info("Dodano nowy protokół o ID: {}", savedProtocol.getId());
                redirectAttributes.addFlashAttribute("message", "Protokół został dodany pomyślnie");
            } else {
                ProtocolDTO updatedProtocol = protocolService.updateProtocol(protocolDTO);
                logger.info("Zaktualizowano protokół o ID: {}", updatedProtocol.getId());
                redirectAttributes.addFlashAttribute("message", "Protokół został zaktualizowany pomyślnie");
            }
        } catch (Exception e) {
            logger.error("Błąd podczas zapisywania protokołu: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas zapisywania protokołu: " + e.getMessage());
            if (protocolDTO.getId() != null) {
                return "redirect:/protocol/form/edit/" + protocolDTO.getId();
            } else {
                return "redirect:/protocol/form/new";
            }
        }

        return "redirect:/protocol";
    }
}