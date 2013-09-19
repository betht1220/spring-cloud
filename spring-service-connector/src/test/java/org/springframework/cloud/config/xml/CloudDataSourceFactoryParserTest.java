package org.springframework.cloud.config.xml;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.cloud.config.DataSourceCloudConfigTestHelper;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public abstract class CloudDataSourceFactoryParserTest extends AbstractCloudServiceConnectorFactoryParserTest<DataSource> {

	protected abstract ServiceInfo createService(String id);
	
	protected String getWithServiceIdContextFileName() {
		return "cloud-datasource-with-service-id.xml";
	}
	
	protected String getWithoutServiceIdContextFileName() {
		return "cloud-datasource-without-service-id.xml";
	}

	protected Class<DataSource> getConnectorType() {
		return DataSource.class;
	}

	// Mixed relational services test (mysql+postgresql)
	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byType() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean("my-service", getConnectorType());
	}

	@Test(expected=BeanCreationException.class)
	public void cloudDataSourceWithoutServiceNameSpecified_TwoMixedServiceExist_byId() {
		ApplicationContext testContext = getTestApplicationContext(getWithoutServiceIdContextFileName(),
				createMysqlService("my-service"), createPostgresqlService("my-service-2"));
		
		testContext.getBean(getConnectorType());
	}
	
	@Test
	public void cloudDataSourceWithMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool20-wait200", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 20, 0, 200);
		
		Properties connectionProp = new Properties();
		connectionProp.put("sessionVariables", "sql_mode='ANSI'");
		connectionProp.put("characterEncoding", "UTF-8");
		DataSourceCloudConfigTestHelper.assertConnectionProperties(ds, connectionProp);
	}
	
	@Test
	public void cloudDataSourceWithMinMaxPool() {
		ApplicationContext testContext = getTestApplicationContext("cloud-datasource-with-config.xml",
				createService("my-service"));
		
		DataSource ds = testContext.getBean("db-pool5-30-wait3000", getConnectorType());
		DataSourceCloudConfigTestHelper.assertPoolProperties(ds, 30, 5, 3000);
	}
}
