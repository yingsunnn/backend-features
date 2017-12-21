package ying.backend_features.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private Long id;

    private String username;

    private String email;

    private Long avatar;

    private String bio;

    private String status;

    private Long createTime;

    private Long updateTime;
}
