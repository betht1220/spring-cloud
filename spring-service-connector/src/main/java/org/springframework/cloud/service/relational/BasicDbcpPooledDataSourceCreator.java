package org.springframework.cloud.service.relational;

import static org.springframework.cloud.service.Util.hasClass;

import javax.sql.DataSource;

import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.RelationalServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 * @param <SI>
 */
public class BasicDbcpPooledDataSourceCreator<SI extends RelationalServiceInfo> extends DbcpLikePooledDataSourceCreator<SI> {

	@Override
	public DataSource create(RelationalServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig,
			                 String driverClassName, String validationQuery) {
		if (hasClass("org.apache.commons.dbcp2.BasicDataSource")) {
            logger.info("Found DBCP2 on the classpath. Using it for DataSource connection pooling.");
            org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
            setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
            return ds;
		} else if (hasClass("org.apache.commons.dbcp.BasicDataSource")) {
            logger.info("Found DBCP on the classpath. Using it for DataSource connection pooling.");
            System.out.println("------ HasClass");
            org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
            System.out.println("------ created ds");
            setBasicDataSourceProperties(ds, serviceInfo, serviceConnectorConfig, driverClassName, validationQuery);
            System.out.println("------ set props");
            return ds;
		} else {
		    System.out.println("------ Found neither dbcp nor dbcp2");
			return null;
		}
	}
}
