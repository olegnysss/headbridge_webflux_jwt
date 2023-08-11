package ryvkin.webflux.jwt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ryvkin.webflux.jwt.domain.car.Car;
import ryvkin.webflux.jwt.repo.CarRepo;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CarService {

    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final CarRepo carRepository;

    public Mono<Car> createCar(Car car){
        return carRepository.save(car);
    }

    public Flux<Car> getAllCars(){
        return carRepository.findAll();
    }

    public Mono<Car> findById(String carId){
        return carRepository.findById(carId);
    }

    public Mono<Car> updateCar(String carId,  Car car){
        return carRepository.findById(carId)
                .flatMap(dbCar -> {
                    dbCar.setName(car.getName());
                    dbCar.setPrice(car.getPrice());
                    dbCar.setModel(car.getModel());
                    return carRepository.save(dbCar);
                });
    }

    public Mono<Car> deleteCar(String carId){
        return carRepository.findById(carId)
                .flatMap(existingCar -> carRepository.delete(existingCar)
                        .then(Mono.just(existingCar)));
    }

    public Flux<Car> fetchCars(String name) {
        Query query = new Query()
                .with(Sort
                        .by(Collections.singletonList(Sort.Order.asc("price")))
                );
        query.addCriteria(Criteria
                .where("name")
                .regex(name)
        );

        return reactiveMongoTemplate
                .find(query, Car.class);
    }
}
