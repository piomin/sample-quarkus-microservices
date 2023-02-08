package pl.piomin.services.employee.controller;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.osgi.ManagedKubernetesClient;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.kubernetes.client.WithKubernetesTestServer;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import pl.piomin.services.employee.model.Employee;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@WithKubernetesTestServer
@QuarkusIntegrationTest
public class EmployeeAppKubernetesIT {

    KubernetesClient client = new KubernetesClientBuilder().build();

    @Test
    void api() throws MalformedURLException {
        Service service = client.services()
                .inNamespace("default")
                .withName("employee-service")
                .get();
        ServicePort port = service.getSpec().getPorts().get(0);
        EmployeeService client = RestClientBuilder.newBuilder()
                .baseUrl(new URL("http://localhost:" + port.getNodePort() + "/employees"))
                .build(EmployeeService.class);
        Employee employee = new Employee(1L, 1L, "Josh Stevens", 23, "Developer");
        employee = client.add(employee);
        assertNotNull(employee.getId());
    }
}
