package ying.backend_features.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ying on 2017-04-17.
 */
@RestController
@RequestMapping(value = "mysql")
public class MySQLController {

    private static Logger logger = LoggerFactory.getLogger(MySQLController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @RequestMapping(value = "test/{userId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String test(@PathVariable("userId") Long userId) {
        StringBuilder sql = new StringBuilder();
        MapSqlParameterSource params = new MapSqlParameterSource();

        sql.append("SELECT ");
        sql.append("  u.user_id, ");
        sql.append("  u.username, ");
        sql.append("  u.profile_picture, ");
        sql.append("  u.bio, ");
        sql.append("  u.gender, ");
        sql.append("  u.region, ");
        sql.append("  u.role, ");
        sql.append("  u.birthday, ");
        sql.append("  u.occupation, ");
        sql.append("  u.user_status, ");
        sql.append("  u.created_at, ");
        sql.append("  u.updated_at ");
        sql.append("FROM t_user u ");
        sql.append("Where u.user_id = :userId");

        params.addValue("userId", userId);
        List<User> users = namedParameterJdbcTemplate.query(sql.toString(), params, new UserMapper());

        String username = "empty";
        if (!CollectionUtils.isEmpty(users))
            username = users.get(0).getUsername();
        return "{\"user\":\"" + username + "\"}";
    }

    private class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setProfilePictureId(rs.getLong("profile_picture"));
            user.setBio(rs.getString("bio"));
            user.setGender(rs.getString("gender"));
            user.setRegionId(rs.getLong("region"));
            user.setRoleId(rs.getLong("role"));
            user.setBirthday(rs.getLong("birthday"));
            user.setOccupation(rs.getString("occupation"));
            user.setUserStatus(rs.getString("user_status"));
            user.setCreatedAt(rs.getLong("created_at"));
            user.setUpdatedAt(rs.getLong("updated_at"));
            return user;
        }
    }
}
