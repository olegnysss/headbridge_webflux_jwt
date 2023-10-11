package ryvkin.webflux.jwt.controller;

import ryvkin.webflux.jwt.domain.User;
import ryvkin.webflux.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final static ResponseEntity<Object> UNAUTHORIZED =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    private final UserService userService;

    @PostMapping("/login")
    public Mono<ResponseEntity> login(ServerWebExchange swe) {
        return swe.getFormData().flatMap(credentials ->
            {
                String username = credentials.getFirst("username");
                String password = credentials.getFirst("password");

                return userService.findByUsername(username)
                    .cast(User.class)
                    .map(userDetails ->
                        Objects.equals(
                            password,
                            userDetails.getPassword()
                        )
                            ? userService.successfulLogIn(userDetails)
                            : userService.unsuccessfullLogIn(username)
                    )
                    .defaultIfEmpty(UNAUTHORIZED);
            }
        );
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> register(@RequestBody User user) {
        return userService.registerUser(user);
    }
}
