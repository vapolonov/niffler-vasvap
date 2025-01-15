package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthAuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthAuthorityEntity... authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      for (AuthAuthorityEntity a : authority) {
        ps.setObject(1, a.getId());
        ps.setString(2, a.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}