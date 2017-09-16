package ying.backend_features.jpa;

import com.github.wenhao.jpa.Specifications;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "jpa", produces = "application/json;charset=UTF-8")
@AllArgsConstructor
@Slf4j
public class JPAController {

    private UserProfileRepository userProfileRepository;

    private PrescriptionRepository prescriptionRepository;

    private DrugRepository drugRepository;

    @PostMapping
    public UserProfile addUserProfile(@Valid @RequestBody UserProfile userProfile) {
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    @GetMapping("{profile_id}")
    public UserProfile getProfileById(@PathVariable("profile_id") Long id) {
        return userProfileRepository.findById(id);
    }

    /**
     * https://127.0.0.1:8443/jpa?name=y&bio=sdf&page=0&size=2&sort=id,asc&sort=name,desc
     *
     * @param id
     * @param name
     * @param bio
     * @param pageable
     * @return
     */
    @GetMapping
    public Page<UserProfile> getUserProfile(@RequestParam(value = "id", required = false) Long id,
                                            @RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "bio", required = false) String bio,
                                            @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Specification<UserProfile> specification = Specifications.<UserProfile>and()
                .eq(id != null, "id", id)
                .like(StringUtils.isNotBlank(name), "name", "%" + name + "%")
                .like(StringUtils.isNotBlank(bio), "bio", "%" + bio + "%")
                .build();

        return userProfileRepository.findAll(specification, pageable);
    }

    /**
     * {
         "title":"p 1",
         "description": "ssdfsfs s fsdf sdf",
         "drugs": [
             {
                "id":1
             },
             {
                "id":2
             }
         ]
       }
     * @param prescription
     * @return
     */
    @PostMapping("prescriptions")
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewPrescription(@Valid @RequestBody Prescription prescription) {
        Prescription result = prescriptionRepository.save(prescription);

        if (CollectionUtils.isNotEmpty(prescription.getDrugs())) {

            List<Drug> drugs = new ArrayList();
            for (Drug drug : prescription.getDrugs()) {
                if (drug.getId() != null){
                    Drug existDrug = drugRepository.findById(drug.getId());
                    drugs.add(existDrug);
                }

            }
            result.setDrugs(drugs);
            drugRepository.save(drugs);
        }
    }

    /**
     *{
         "name": "drug 000",
         "description": "sf sdf sd asf 23 2"
      }
     * @param drug
     * @return
     */
    @PostMapping("drugs")
    public void addNewDrug (@Valid @RequestBody Drug drug) {
        drugRepository.save(drug);
    }

    @GetMapping("prescriptions/drugs")
    @Transactional(readOnly = true)
    public List<Prescription> getPrescriptionByDrug (@RequestParam(value = "drug_id", required = false) Long drugId) {
        List<Prescription> prescriptions = prescriptionRepository.findByDrugs_Id(drugId);

        for (Prescription prescription : prescriptions) {
            for (Drug drug : prescription.getDrugs()){
                drug.setPrescriptions(null);
            }
        }
        return prescriptions;
    }

    @GetMapping("prescriptions/{prescription_id}")
    @Transactional(readOnly = true)
    public List<Prescription> getPresciption (@PathVariable("prescription_id") Long id) {
        List<Prescription> prescriptions = prescriptionRepository.findById(id);

        for (Prescription prescription : prescriptions) {
            for (Drug drug : prescription.getDrugs()){
                drug.setPrescriptions(null);
            }
        }
        return prescriptions;
    }
}
