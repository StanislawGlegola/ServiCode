package pl.sg.servicode.module.device.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import pl.sg.servicode.module.device.DTO.DeviceDTO;
import pl.sg.servicode.module.device.entity.DeviceEntity;
import pl.sg.servicode.module.device.mapper.DeviceMapper;
import pl.sg.servicode.module.device.repository.DeviceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    public List<DeviceDTO> getDeviceList() {
        List<DeviceEntity> devices = deviceRepository.findAll();
        return devices.stream()
                .map(deviceMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO getDeviceById(int id) {
        DeviceEntity device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono urządzenia o ID: " + id));
        return deviceMapper.toDTO(device);
    }

    public void deleteDevice(int id) {
        deviceRepository.deleteById(id);
    }

    public DeviceDTO addNewDevice(@Valid DeviceDTO deviceDTO) {
        DeviceEntity device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDTO(device);
    }

    public DeviceDTO updateDevice(@Valid DeviceDTO deviceDTO) {
        if (deviceDTO.getId() == null) {
            throw new RuntimeException("ID urządzenia nie może być puste przy aktualizacji");
        }
        DeviceEntity device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDTO(device);
    }

    public List<DeviceDTO> getDevicesByCustomerId(Integer customerId) {
        List<DeviceEntity> devices = deviceRepository.findByCustomerId(customerId);
        return devices.stream()
                .map(deviceMapper::toDTO)
                .collect(Collectors.toList());
    }
}