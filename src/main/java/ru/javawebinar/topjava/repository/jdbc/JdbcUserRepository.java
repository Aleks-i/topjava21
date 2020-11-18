package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidatorForJdbc.validate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final Logger log = getLogger(JdbcUserRepository.class);

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        jdbcTemplate.update("DELETE FROM USER_ROLES WHERE USER_ID=?", user.getId());
        batchInsertRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        if (users.isEmpty()) {
            return null;
        } else {
            User user = DataAccessUtils.singleResult(users);
            user.setRoles(getRolesForUser(id));
            return user;
        }
    }

    @Override
    public User getByEmail(String email) {
        validate(email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        if (users.isEmpty()) {
            return null;
        } else {
            User user = DataAccessUtils.singleResult(users);
            assert user != null;
            user.setRoles(getRolesForUser(user.getId()));
            return user;
        }
    }

    @Override
    public List<User> getAll() {
        Map<Integer, Set<Role>> rolesMap = getAllRoles();
        return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER).stream()
                .peek(user -> user.setRoles(rolesMap.get(user.getId())))
                .collect(Collectors.toList());
    }

    private List<Role> getRolesForUser(int userId) {
        return jdbcTemplate.queryForList("SELECT role r FROM USER_ROLES WHERE USER_ID=?", Role.class, userId);
    }

    private Map<Integer, Set<Role>> getAllRoles() {
        Map<Integer, Set<Role>> mapWithRoles = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM USER_ROLES", new ResultSetExtractor<Map>() {
            @Override
            public Map extractData(ResultSet rs) throws SQLException, DataAccessException {
                while (rs.next()) {
                    Integer userId = rs.getInt("user_id");
                    Role role = Role.valueOf(rs.getString("role"));
                    Set<Role> roles = mapWithRoles.get(userId);
                    if (roles == null) {
                        roles = new HashSet<Role>();
                        roles.add(role);
                    }
                    Set<Role> finalRoles = roles;
                    mapWithRoles.merge(userId, roles,
                            (a, b) -> getRoles(finalRoles, role));
                }
                return mapWithRoles;
            }
        });
        return mapWithRoles;
    }

    private Set<Role> getRoles(Set<Role> roles, Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
        return roles;
    }

    private void batchInsertRoles(User user) {
        List<Role> roles = new ArrayList<>(user.getRoles());
        this.jdbcTemplate.batchUpdate(
                "insert into user_roles (user_id, role) values(?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Role role = roles.get(i);
                        ps.setInt(1, user.getId());
                        ps.setString(2, String.valueOf(role));
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }
}
