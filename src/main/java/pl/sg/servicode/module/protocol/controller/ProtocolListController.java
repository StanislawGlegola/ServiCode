package pl.sg.servicode.module.protocol.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;
import pl.sg.servicode.module.protocol.service.ProtocolPdfFiller;
import pl.sg.servicode.module.protocol.service.ProtocolService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
            logger.error("Error during loading protocols: {}", e.getMessage());
            model.addAttribute("error", "Error during loading protocols: " + e.getMessage());
            return "protocol/list";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProtocol(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            protocolService.deleteProtocol(id);
            logger.info("Protocol with ID {} deleted successfully", id);
            redirectAttributes.addFlashAttribute("message", "Protocol deleted successfully");
        } catch (Exception e) {
            logger.error("Error during deleting protocol with ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Error during deleting protocol: " + e.getMessage());
        }
        return "redirect:/protocol";
    }

    @GetMapping("/{id}/pdf")
    public void generatePdf(@PathVariable int id, HttpServletResponse response) {
        try {
            var dto = protocolService.getProtocolById(id);

            // Wczytanie szablonu z classpath jako InputStream
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("templates/Protokol_serwisowo-konserwacyjny_edytowalny.pdf");
            if (inputStream == null) {
                throw new FileNotFoundException("Nie znaleziono szablonu PDF w zasobach.");
            }

            // Zapis InputStream do pliku tymczasowego
            Path tempInputPath = Files.createTempFile("protocol_template", ".pdf");
            Files.copy(inputStream, tempInputPath, StandardCopyOption.REPLACE_EXISTING);
            File inputFile = tempInputPath.toFile();

            // Plik wyjściowy
            File outputFile = File.createTempFile("protokol_" + id + "_", ".pdf");

            // Wypełnianie PDF
            new ProtocolPdfFiller().fillPdfFromDto(dto, inputFile, outputFile);
            System.out.println(dto.toString());
            // Konfiguracja odpowiedzi HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=%s".formatted(outputFile.getName()));
            Files.copy(outputFile.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            logger.error("PDF generation failed for ID {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

}