package pl.sg.servicode.module.protocol.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;
import pl.sg.servicode.module.protocol.service.ProtocolService;

import java.util.List;

@Controller
@RequestMapping("/protocol")
public class ProtocolListController {
    private static final Logger logger = LoggerFactory.getLogger(ProtocolListController.class);

    private final ProtocolService protocolService;

    public ProtocolListController(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    @GetMapping
    public String showProtocolList(Model model) {
        try {
            List<ProtocolDTO> protocols = protocolService.getProtocolList();
            model.addAttribute("protocols", protocols);
            model.addAttribute("activeTab", "protocol");
            return "protocol/list";
        } catch (Exception e) {
            logger.error("Błąd podczas ładowania listy protokołów: {}", e.getMessage());
            model.addAttribute("error", "Wystąpił błąd podczas ładowania danych");
            return "protocol/list";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProtocol(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            protocolService.deleteProtocol(id);
            logger.info("Usunięto protokół o ID: {}", id);
            redirectAttributes.addFlashAttribute("message", "Protokół został usunięty pomyślnie");
        } catch (Exception e) {
            logger.error("Błąd podczas usuwania protokołu o ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Wystąpił błąd podczas usuwania protokołu: " + e.getMessage());
        }
        return "redirect:/protocol";
    }
}