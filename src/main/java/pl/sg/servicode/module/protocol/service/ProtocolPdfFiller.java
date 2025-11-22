package pl.sg.servicode.module.protocol.service;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProtocolPdfFiller {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void fillPdfFromDto(ProtocolDTO dto, File inputFile, File outputFile) {
        try (PDDocument pdfDocument = PDDocument.load(inputFile)) {

            PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IllegalStateException("Formularz PDF nie zawiera pola AcroForm");
            }
            
            // Debug: wypisz wszystkie dostępne pola w formularzu
            System.out.println("=== Dostępne pola w formularzu PDF ===");
            List<PDField> fields = acroForm.getFields();
            for (PDField field : fields) {
                System.out.println("Pole: " + field.getFullyQualifiedName() + " (typ: " + field.getClass().getSimpleName() + ")");
            }
            System.out.println("========================================");

            // Użycie czcionki systemowej, która obsługuje polskie znaki
            PDType0Font font = null;
            String fontName = null;
            
            // Lista czcionek systemowych do wypróbowania (w kolejności priorytetu)
            String[] fontNames = {"Arial", "Calibri", "Tahoma", "Verdana", "Times New Roman", "DejaVu Sans"};
            String fontsPath = System.getenv("WINDIR");
            if (fontsPath == null) {
                fontsPath = "C:/Windows";
            }
            fontsPath += "/Fonts/";
            
            try {
                for (String name : fontNames) {
                    // Różne warianty nazw plików czcionek
                    String[] fileVariants = {
                        name.toLowerCase() + ".ttf",
                        name + ".ttf",
                        name.toUpperCase() + ".TTF",
                        name.replace(" ", "") + ".ttf"
                    };
                    
                    File fontFile = null;
                    for (String variant : fileVariants) {
                        Path fontPath = Paths.get(fontsPath + variant);
                        if (Files.exists(fontPath)) {
                            fontFile = fontPath.toFile();
                            break;
                        }
                    }
                    
                    if (fontFile != null && fontFile.exists()) {
                        try {
                            font = PDType0Font.load(pdfDocument, fontFile);
                            fontName = name.replace(" ", "");
                            
                            // Rejestracja czcionki w zasobach formularza
                            PDResources resources = acroForm.getDefaultResources();
                            if (resources == null) {
                                resources = new PDResources();
                                acroForm.setDefaultResources(resources);
                            }
                            COSName fontResourceName = COSName.getPDFName(fontName);
                            resources.put(fontResourceName, font);
                            
                            System.out.println("Załadowano czcionkę: " + name);
                            break;
                        } catch (Exception e) {
                            System.err.println("Nie udało się załadować czcionki " + name + ": " + e.getMessage());
                            continue;
                        }
                    }
                }
                
                if (font == null) {
                    throw new IllegalStateException("Nie znaleziono żadnej czcionki systemowej obsługującej polskie znaki");
                }
            } catch (Exception e) {
                throw new RuntimeException("Błąd przy ładowaniu czcionki systemowej: " + e.getMessage(), e);
            }

            // Wypełnianie pól formularza – nazwy muszą odpowiadać tym z edytowalnego PDF
            set(acroForm, "Text-HkgrlrklSb", formatDate(dto.getStartDate()), fontName);
            set(acroForm, "Text-VQxXrYDbvD", formatDate(dto.getEndDate()), fontName);
            set(acroForm, "Text-KGwZeiWLtz", dto.getIssueDescription(), fontName);
            set(acroForm, "Paragraph-JeS_s7L20T", dto.getWorkDescription(), fontName);
            set(acroForm, "Text-ePHyfrQYRo", dto.getMaterials(), fontName);
            set(acroForm, "Text-P-1PqjaWf1", dto.getNotes(), fontName);
            set(acroForm, "Text-B0snUbqs-i", dto.getCustomerName(), fontName);
            set(acroForm, "Text-RcDO8Ypfob", dto.getDeviceSerialNumber(), fontName);

            // Technicy – złączone imiona i nazwiska + f-gaz
            StringBuilder technicianText = new StringBuilder();
            if (dto.getTechnicians() != null) {
                dto.getTechnicians().forEach(tech ->
                        technicianText.append(tech.getFirstName())
                                .append(" ")
                                .append(tech.getLastName())
                                .append(" (")
                                .append(tech.getFGaz() != null ? tech.getFGaz() : "")
                                .append(")\n"));
            }
            set(acroForm, "technicians", technicianText.toString().trim(), fontName);

            acroForm.flatten(); // spłaszczenie formularza (opcjonalne)

            pdfDocument.save(outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Błąd przy generowaniu PDF: " + e.getMessage(), e);
        }
    }

    private void set(PDAcroForm form, String fieldName, String value, String fontName) throws IOException {
        if (value == null || value.isEmpty()) {
            return;
        }
        
        var field = form.getField(fieldName);
        if (field == null) {
            System.err.println("Ostrzeżenie: Pole '" + fieldName + "' nie zostało znalezione w formularzu PDF");
            return;
        }

        if (field instanceof PDTextField textField) {
            // Ustaw czcionkę dla tego konkretnego pola przed ustawieniem wartości
            // Format: /FontName Size Tf Color
            textField.setDefaultAppearance("/" + fontName + " 10 Tf 0 g");
            
            // Ustaw wartość
            textField.setValue(value);
        }
    }

    private String formatDate(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "";
    }
}