package ryvkin.webflux.jwt.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Service;

@Service
public class MetricService {
    final Counter registerCounter = Metrics.counter("register_counter");
    final Counter loginCounter = Metrics.counter("login_counter");
    final Counter failedLoginCounter = Metrics.counter("failed_login_counter");

    public void incrementRegisterCounter() {
        registerCounter.increment();
    }
    public void incrementLoginCounter() {
        loginCounter.increment();
    }
    public void incrementFailedLoginCounter() {
        failedLoginCounter.increment();
    }
}
