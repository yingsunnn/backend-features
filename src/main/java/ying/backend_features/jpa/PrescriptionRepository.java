package ying.backend_features.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long>, JpaSpecificationExecutor<Prescription> {

}
