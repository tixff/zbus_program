package com.ti.zbus_manager;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.test.context.TestContext;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class MyDbUnitTestExecutionListener extends DbUnitTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        createTables(testContext);

        super.beforeTestMethod(testContext);
    }

    private void createTables(TestContext testContext) throws Exception {
        // get db connection
        DataSource dataSource = (DataSource) testContext.getApplicationContext().getBean(DataSource.class);

        try (Connection connection = dataSource.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            InputStream resourceAsStream = getClass().getResourceAsStream("/data/ddl.sql");
            InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
            scriptRunner.runScript(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
