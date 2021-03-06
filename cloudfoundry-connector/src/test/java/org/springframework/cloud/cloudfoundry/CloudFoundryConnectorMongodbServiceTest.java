package org.springframework.cloud.cloudfoundry;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.springframework.cloud.service.ServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class CloudFoundryConnectorMongodbServiceTest extends AbstractCloudFactoryConnectorTest {
	@Test
	public void mongoServiceCreation() {
		String[] versions = {"2.0", "2.2"};
		for (String version : versions) {
			when(mockEnvironment.getEnvValue("VCAP_SERVICES"))
				.thenReturn(getServicesPayload(
						getMongoServicePayload(version, "mongo-1", hostname, port, username, password, "inventory-1", "db"),
						getMongoServicePayload(version, "mongo-2", hostname, port, username, password, "inventory-2", "db")));
		}

		List<ServiceInfo> serviceInfos = testCloudConnector.getServiceInfos();
		assertNotNull(getServiceInfo(serviceInfos, "mongo-1"));
		assertNotNull(getServiceInfo(serviceInfos, "mongo-2"));
	}

	private String getMongoServicePayload(String version, String serviceName,
                                			 String hostname, int port,
                                			 String username, String password, String db, String name) {
		String payload = readTestDataFile("test-mongodb-info.json");
		payload = payload.replace("$version", version);
		payload = payload.replace("$serviceName", serviceName);
		payload = payload.replace("$hostname", hostname);
		payload = payload.replace("$port", Integer.toString(port));
		payload = payload.replace("$username", username);
		payload = payload.replace("$password", password);
		payload = payload.replace("$db", db);
		payload = payload.replace("$name", name);
		
		return payload;
	}

	
}
