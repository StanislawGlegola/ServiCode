package pl.sg.servicode.module.device.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.device.DTO.DeviceDTO;
import pl.sg.servicode.module.device.service.DeviceService;
import pl.sg.servicode.module.customer.service.CustomerService;

import java.util.List;

@Controller
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;
    private final CustomerService customerService;

    public DeviceController(DeviceService deviceService, CustomerService customerService) {
        this.deviceService = deviceService;
        this.customerService = customerService;
    }

    @GetMapping("/by-customer/{customerId}")
    @ResponseBody
    public List<DeviceDTO> getDevicesByCustomerId(@PathVariable Integer customerId) {
        return deviceService.getDevicesByCustomerId(customerId);
    }

    @GetMapping
    public String showDeviceList(Model model) {
        List<DeviceDTO> devices = deviceService.getDeviceList();
        model.addAttribute("devices", devices);
        model.addAttribute("customers", customerService.getCustomerList());
        model.addAttribute("device", new DeviceDTO());
        model.addAttribute("activeTab", "device");
        return "devices";
    }

    @PostMapping("/save")
    public String saveDevice(@ModelAttribute @Valid DeviceDTO deviceDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/device";
        }

        if (deviceDTO.getId() == null) {
            deviceService.addNewDevice(deviceDTO); // Nowe urządzenie
            redirectAttributes.addFlashAttribute("message", "Nowe urządzenie dodane!");
        } else {
            deviceService.updateDevice(deviceDTO); // Edycja istniejącego urządzenia
            redirectAttributes.addFlashAttribute("message", "Dane urządzenia zaktualizowane!");
        }
        return "redirect:/device";
    }

    @GetMapping("/delete/{id}")
    public String deleteDevice(@PathVariable int id, RedirectAttributes redirectAttributes) {
        deviceService.deleteDevice(id);
        redirectAttributes.addFlashAttribute("message", "Urządzenie zostało usunięte pomyślnie!");
        return "redirect:/device";
    }
}
