package pl.sg.servicode.module.protocol.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sg.servicode.module.protocol.DTO.ProtocolDTO;
import pl.sg.servicode.module.protocol.entity.ProtocolEntity;
import pl.sg.servicode.module.protocol.mapper.ProtocolMapper;
import pl.sg.servicode.module.protocol.repository.ProtocolRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProtocolService {

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper protocolMapper;

    public ProtocolService(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    public List<ProtocolDTO> getProtocolList() {
        return protocolRepository.findAll().stream()
                .map(protocolMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProtocolDTO getProtocolById(int id) {
        return protocolRepository.findById(id)
                .map(protocolMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Object not found"));
    }

    public void deleteProtocol(int id) {
        protocolRepository.deleteById(id);
    }

    @Transactional
    public ProtocolDTO addNewProtocol(@Valid ProtocolDTO protocolDTO) {
        ProtocolEntity entity = protocolMapper.toEntity(protocolDTO);
        entity = protocolRepository.save(entity);
        return protocolMapper.toDTO(entity);
    }

    @Transactional
    public ProtocolDTO updateProtocol(@Valid ProtocolDTO protocolDTO) {
        ProtocolEntity entity = protocolMapper.toEntity(protocolDTO);
        entity = protocolRepository.save(entity);
        return protocolMapper.toDTO(entity);
    }
} 