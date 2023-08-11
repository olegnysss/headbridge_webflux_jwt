package ryvkin.webflux.jwt.repo;

import ryvkin.webflux.jwt.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepo extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsername(String name);
}
