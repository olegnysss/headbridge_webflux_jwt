package ryvkin.webflux.jwt.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ryvkin.webflux.jwt.config.JwtUtil;
import ryvkin.webflux.jwt.domain.User;
import ryvkin.webflux.jwt.producer.Producer;
import ryvkin.webflux.jwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService implements ReactiveUserDetailsService {
    private final UserRepo userRepo;
    private final Producer producer;
    private final JwtUtil jwtUtil;
    private final MetricService metricService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        producer.sendEvent(username, formatMessage(username, "find user in database"));
        return userRepo.findByUsername(username)
                .cast(UserDetails.class);
    }

    public Mono<User> registerUser(User user) {
        String username = user.getUsername();
        producer.sendEvent(username, formatMessage(username, "registered new user"));
        metricService.incrementRegisterCounter();
        return userRepo.save(user)
            .cast(User.class);
    }

    public ResponseEntity<Object> successfulLogIn(User user) {
        String username = user.getUsername();
        producer.sendEvent(username, formatMessage(username, "Successful log in"));
        metricService.incrementLoginCounter();
        return ResponseEntity.ok(jwtUtil.generateToken(user));
    }

    public ResponseEntity<Object> unsuccessfullLogIn(String username) {
        producer.sendEvent(username, formatMessage(username, "Unsuccessful log in"));
        metricService.incrementFailedLoginCounter();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String formatMessage(String username, String message) {
        return String.format("Message '%s' for user %s", message, username);
    }
}
