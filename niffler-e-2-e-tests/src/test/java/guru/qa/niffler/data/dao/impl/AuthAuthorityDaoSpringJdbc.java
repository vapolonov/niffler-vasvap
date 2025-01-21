package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuthAuthorityDaoSpringJdbc implements AuthAuthorityDao {

    // в таблице Authority содержаться данные о правах пользователя, которые назначаются ему при регистрации
    // по умолчанию назначаются 2 Authority - read / write
    // получается нужно вставить две строчки в таблицу, поэтому в методе create в параметрах используется vararg (аргумент переменной/произвольной длины)
    // массив Authority... чтобы сразу вставить в таблицу две записи
    // несколько записей (в случае Jdbc или SpringJdbc) вставляются при помощи BatchUpdate - возможность выполнить
    // обновление сразу нескольких строчек одним запросом

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.batchUpdate(
                "INSERT INTO authority (user_id, authority) VALUES (?, ?)",
                // для выполнения BatchUpdate будем использовать интерфейс BatchPreparedStatementSetter
                // позволяет описать два метода благодаря которым SpringJdbc сам разложит массив наших объектов на SQL.
                // BatchPreparedStatementSetter - не является функциональным интерфейсом (нет аннотации @FunctionalInterface)
                // т.е. мы можем создавать объекты данного интерфейса, а также создать анонимную реализацию этого интерфейса:
                new BatchPreparedStatementSetter() {

                    // в данный метод приходят параметры PreparedStatement и индекс той строчки, которую мы в данных момент вставляем
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        // по индексу будут вставлены user_id и authority
                        ps.setObject(1, authority[i].getUserId());
                        ps.setString(2, authority[i].getAuthority().name());
                    }

                    // возвращает сколько строчек мы хотим вставить в таблицу
                    @Override
                    public int getBatchSize() {
                        return authority.length;
                    }
                }
        );
    }
}
