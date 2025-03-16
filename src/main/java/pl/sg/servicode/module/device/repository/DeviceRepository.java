package pl.sg.servicode.module.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.servicode.module.device.entity.DeviceEntity;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Integer> {
    List<DeviceEntity> findByCustomerId(Integer customerId);
}
