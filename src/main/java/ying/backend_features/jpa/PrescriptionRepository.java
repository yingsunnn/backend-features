package ying.backend_features.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrescriptionRepository
        extends JpaRepository<Prescription, Long>, JpaSpecificationExecutor<Prescription> {


    List<Prescription> findByDrugs_Id (Long id);

//    @Query("select p from t_prescription p where p.id = :id" )
//    List<Prescription> findById (@Param("id")Long id);

    List<Prescription> findById (Long id);
}
