package ryvkin.webflux.jwt.service;

import ryvkin.webflux.jwt.domain.User;
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

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepo.findByUsername(username)
                .cast(UserDetails.class);
    }

    public Mono<User> registerUser(User user) {
        return userRepo.save(user)
            .cast(User.class);
    }
}
