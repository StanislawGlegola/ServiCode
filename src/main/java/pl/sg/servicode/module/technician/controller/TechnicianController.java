package pl.sg.servicode.module.technician.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.technician.DTO.TechnicianDTO;
import pl.sg.servicode.module.technician.service.TechnicianService;

import java.util.List;

@Controller
@RequestMapping("/technician")
public class TechnicianController {
    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GetMapping
    public String showTechnicianList(Model model) {
        List<TechnicianDTO> technicians = technicianService.getTechnicianList();
        model.addAttribute("technicians", technicians);
        model.addAttribute("technician", new TechnicianDTO());
        model.addAttribute("activeTab", "technician");
        return "technicians";
    }

    @PostMapping("/save")
    public String saveTechnician(@ModelAttribute @Valid TechnicianDTO technicianDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "redirect:/technician";
        }

        if (technicianDTO.getId() == null) {
            technicianService.addNewTechnician(technicianDTO);
            redirectAttributes.addFlashAttribute("message", "Nowy technik dodany!");
        } else {
            technicianService.updateTechnician(technicianDTO);
            redirectAttributes.addFlashAttribute("message", "Dane technika zaktualizowane!");
        }
        return "redirect:/technician";
    }

    @GetMapping("/delete/{id}")
    public String deleteTechnician(@PathVariable int id, RedirectAttributes redirectAttributes) {
        technicianService.deleteTechnician(id);
        redirectAttributes.addFlashAttribute("message", "Technik został usunięty pomyślnie!");
        return "redirect:/technician";
    }
} 