package ying.backend_features.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DrugRepository
        extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug> {

    Drug findById (Long id);

}
