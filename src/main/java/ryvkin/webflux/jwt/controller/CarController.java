package ryvkin.webflux.jwt.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ryvkin.webflux.jwt.domain.car.Car;
import ryvkin.webflux.jwt.service.CarService;

import java.time.Duration;
import java.util.stream.Stream;

@RequiredArgsConstructor
@RestController
@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080")},
    info = @Info(title = "Sample Spring Boot + WebFlux + Spring Security", version = "v1",
        description = "A demo project for TeachBase Java Senior Course",
        contact = @Contact(url = "https://github.com/olegnysss/", name = "Oleg Ryvkin")))
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Flux<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{carId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<ResponseEntity<Car>> getCarById(@PathVariable String carId) {
        Mono<Car> car = carService.findById(carId);
        return car.map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Car> create(@RequestBody Car car) {
        return carService.createCar(car);
    }

    @PutMapping("/{carId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<ResponseEntity<Car>> updateCarById(@PathVariable String carId, @RequestBody Car car) {
        return carService.updateCar(carId, car)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{carId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Mono<ResponseEntity<Void>> deleteCarById(@PathVariable String carId) {
        return carService.deleteCar(carId)
            .map(r -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Flux<Car> searchCars(@RequestParam("name") String name) {
        return carService.fetchCars(name);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Flux<Car> streamAllCars() {
        return carService
            .getAllCars()
            .flatMap(car -> Flux
                .zip(Flux.interval(Duration.ofSeconds(2)),
                    Flux.fromStream(Stream.generate(() -> car))
                )
                .map(Tuple2::getT2)
            );
    }
}
