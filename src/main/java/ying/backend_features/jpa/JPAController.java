package ying.backend_features.jpa;

import com.github.wenhao.jpa.Specifications;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "jpa", produces = "application/json;charset=UTF-8")
@AllArgsConstructor
@Slf4j
public class JPAController {

    private UserProfileRepository userProfileRepository;

    @PostMapping
    public UserProfile addUserProfile (@Valid @RequestBody UserProfile userProfile) {
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    @GetMapping("{profile_id}")
    public UserProfile getProfileById (@PathVariable("profile_id") Long id) {
        return userProfileRepository.findById(id);
    }

    /**
     * https://127.0.0.1:8443/jpa?name=y&bio=sdf&page=0&size=2&sort=id,asc&sort=name,desc
     * @param id
     * @param name
     * @param bio
     * @param pageable
     * @return
     */
    @GetMapping
    public Page<UserProfile> getUserProfile (@RequestParam(value = "id", required = false) Long id,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "bio", required = false) String bio,
                                             @PageableDefault(size = 10, sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
        Specification<UserProfile> specification = Specifications.<UserProfile>and()
                .eq(id != null, "id", id)
                .like(StringUtils.isNotBlank(name), "name", "%" + name + "%")
                .like(StringUtils.isNotBlank(bio), "bio", "%" + bio + "%")
                .build();

        return userProfileRepository.findAll(specification, pageable);
    }
}
