package pl.sg.servicode.module.technician.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.servicode.module.technician.entity.TechnicianEntity;

@Repository
public interface TechnicianRepository extends JpaRepository<TechnicianEntity, Integer> {
} 