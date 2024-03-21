package edu.java.scrapper.integrationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseTest extends IntegrationTest {
    @Test
    public void checkDatabase() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", POSTGRES.getUsername());
        properties.put("password", POSTGRES.getPassword());
        Connection connection = DriverManager.getConnection(
            POSTGRES.getJdbcUrl(),
            properties
        );

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData
            .getTables(null, null, null, new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (resultSet.next()) {
            tables.add(resultSet.getString("TABLE_NAME"));
        }

        Assertions.assertEquals(
            tables,
            List.of("chat", "connections", "databasechangelog", "databasechangeloglock", "link")
        );
    }
}
