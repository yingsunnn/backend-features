package ying.backend_features.mysql;

/**
 * Created by ying on 2017-04-17.
 *
 * CREATE TABLE `t_user` (
     `user_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
     `username` VARCHAR(100) NULL,
     `profile_picture` BIGINT(10) NULL,
     `bio` VARCHAR(200) NULL,
     `gender` VARCHAR(20) NULL,
     `region` BIGINT(10) NULL,
     `role` BIGINT(10) NULL,
     `birthday` BIGINT(15) NULL,
     `occupation` VARCHAR(200) NULL,
     `user_status` VARCHAR(50) NULL,
     `created_at` BIGINT(15) NULL,
     `updated_at` BIGINT(15) NULL,
     PRIMARY KEY (`user_id`)
     );
 */
public class User {
    private Long userId;
    private String username;
    private Long profilePictureId;
    private String bio;
    private String gender;
    private Long regionId;
    private Long roleId;
    private Long birthday;
    private String occupation;
    private String userStatus;
    private Long createdAt;
    private Long updatedAt;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(Long profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }
}
