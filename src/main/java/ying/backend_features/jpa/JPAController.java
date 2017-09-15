package ying.backend_features.jpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
