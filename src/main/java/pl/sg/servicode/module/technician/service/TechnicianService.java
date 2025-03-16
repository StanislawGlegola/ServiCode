package pl.sg.servicode.module.technician.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import pl.sg.servicode.module.technician.DTO.TechnicianDTO;
import pl.sg.servicode.module.technician.entity.TechnicianEntity;
import pl.sg.servicode.module.technician.mapper.TechnicianMapper;
import pl.sg.servicode.module.technician.repository.TechnicianRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;
    private final TechnicianMapper technicianMapper;

    public TechnicianService(TechnicianRepository technicianRepository, TechnicianMapper technicianMapper) {
        this.technicianRepository = technicianRepository;
        this.technicianMapper = technicianMapper;
    }

    public List<TechnicianDTO> getTechnicianList() {
        return technicianRepository.findAll().stream()
                .map(technicianMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TechnicianDTO getTechnicianById(int id) {
        return technicianRepository.findById(id)
                .map(technicianMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Object not found"));
    }

    public void deleteTechnician(int id) {
        technicianRepository.deleteById(id);
    }

    public void addNewTechnician(@Valid TechnicianDTO technicianDTO) {
        TechnicianEntity entity = technicianMapper.toEntity(technicianDTO);
        technicianRepository.save(entity);
    }

    public void updateTechnician(@Valid TechnicianDTO technicianDTO) {
        TechnicianEntity entity = technicianMapper.toEntity(technicianDTO);
        technicianRepository.save(entity);
    }
} 