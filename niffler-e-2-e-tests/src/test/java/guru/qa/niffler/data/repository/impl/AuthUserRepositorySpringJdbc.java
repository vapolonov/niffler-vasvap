package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, keyHolder);
        final UUID id = (UUID) keyHolder.getKeys().get("id");
        user.setId(id);

        List<AuthorityEntity> authorities = user.getAuthorities();

        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (? , ?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, authorities.get(i).getId());
                        ps.setString(2, authorities.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return authorities.size();
                    }
                }
        );
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        "SELECT * from \"user\" u join authority a on u.id = a.user_id where u.id = ?",
                        new ResultSetExtractor<AuthUserEntity>() {
                            @Override
                            public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
                                Map<UUID, AuthUserEntity> users = new ConcurrentHashMap<>();
                                UUID userId = null;
                                while (rs.next()) {
                                    userId = rs.getObject("id", UUID.class);
                                    AuthUserEntity user = users.computeIfAbsent(userId, id -> {
                                        AuthUserEntity authUser = new AuthUserEntity();
                                        authUser.setId(id);
                                        try {
                                            authUser.setUsername(rs.getString("username"));
                                            authUser.setPassword(rs.getString("password"));
                                            authUser.setEnabled(rs.getBoolean("enabled"));
                                            authUser.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                                            authUser.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                                            authUser.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        return authUser;
                                    });
                                    AuthorityEntity authority = new AuthorityEntity();
                                    authority.setId(rs.getObject("id", UUID.class));
                                    authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                                    user.addAuthorities(authority);
                                }
                                return users.get(userId);
                            }
                        },
                        id
                )
        );
    }

    @Override
    public List<AuthUserEntity> findAll() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "select * from \"user\" u join authority a on u.id = a.user_id",
                new ResultSetExtractor<List<AuthUserEntity>>() {
                    @Override
                    public List<AuthUserEntity> extractData(ResultSet rs) throws SQLException, DataAccessException {
                        List<AuthUserEntity> users = new ArrayList<>();
                        while (rs.next()) {
                            AuthUserEntity user = new AuthUserEntity();
                            AuthorityEntity authority = new AuthorityEntity();
                            user.setId(rs.getObject("id", UUID.class));
                            user.setUsername(rs.getString("username"));
                            user.setPassword(rs.getString("password"));
                            user.setEnabled(rs.getBoolean("enabled"));
                            user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
                            user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
                            authority.setId(rs.getObject("id", UUID.class));
                            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
                            user.addAuthorities(authority);
                            users.add(user);
                        }
                        return users;
                    }
                }
        );
    }
}