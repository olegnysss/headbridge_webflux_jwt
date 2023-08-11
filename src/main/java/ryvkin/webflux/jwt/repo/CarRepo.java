package ryvkin.webflux.jwt.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ryvkin.webflux.jwt.domain.car.Car;

@Repository
public interface CarRepo extends ReactiveMongoRepository<Car, String> {
}
