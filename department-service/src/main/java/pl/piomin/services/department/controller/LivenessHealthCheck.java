package pl.piomin.services.department.controller;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Liveness
public class LivenessHealthCheck implements HealthCheck  {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Employee Health Check").up().build();
    }
    
}
