package ying.backend_features.jpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
