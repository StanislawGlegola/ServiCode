package pl.sg.servicode.module.protocol.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.servicode.module.protocol.entity.ProtocolEntity;

@Repository
public interface ProtocolRepository extends JpaRepository<ProtocolEntity, Integer> {
} 