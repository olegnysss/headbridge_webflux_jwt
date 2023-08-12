package ryvkin.webflux.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ryvkin.webflux.jwt.controller.CarController;
import ryvkin.webflux.jwt.domain.car.Car;
import ryvkin.webflux.jwt.domain.car.CarManufacturer;
import ryvkin.webflux.jwt.service.CarService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CarController.class)
@Import(WebFluxSecurityConfig.class)
class WebFluxApplicationTests {
    @MockBean
    CarService service;
    @MockBean
    ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testCreateCar() {

        Car car = new Car();
        car.setId("64d2a17acc1bdf7e9cac36a1");
        car.setName("TT");
        car.setModel(CarManufacturer.AUDI);
        car.setPrice(30000L);

        Mockito.when(service.createCar(car)).thenReturn(Mono.just(car));

        webClient
            .post()
            .uri("/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(car))
            .exchange()
            .expectStatus().isCreated();

        Mockito.verify(service, times(1)).createCar(car);
    }

    @Test
    void testGetCarsByName() {
        Car car = new Car();
        car.setId("64d2a17acc1bdf7e9cac36a1");
        car.setName("TT");
        car.setModel(CarManufacturer.AUDI);
        car.setPrice(30000L);

        List<Car> list = new ArrayList<>();
        list.add(car);

        Flux<Car> carFlux = Flux.fromIterable(list);

        Mockito
            .when(service.fetchCars("TT"))
            .thenReturn(carFlux);

        webClient.get().uri("/cars/search?name={name}", "TT")
            .header(HttpHeaders.ACCEPT, "application/json")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Car.class);

        Mockito.verify(service, times(1)).fetchCars("TT");
    }

    @Test
    void testGetCarById() {
        Car car = new Car();
        car.setId("64d2a17acc1bdf7e9cac36a1");
        car.setName("TT");
        car.setModel(CarManufacturer.AUDI);
        car.setPrice(30000L);

        Mockito
            .when(service.findById("64d2a17acc1bdf7e9cac36a1"))
            .thenReturn(Mono.just(car));

        webClient.get().uri("/cars/{id}", "64d2a17acc1bdf7e9cac36a1")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.name").isNotEmpty()
            .jsonPath("$.id").isEqualTo("64d2a17acc1bdf7e9cac36a1")
            .jsonPath("$.name").isEqualTo("TT")
            .jsonPath("$.price").isEqualTo(30000);

        Mockito.verify(service, times(1)).findById("64d2a17acc1bdf7e9cac36a1");
    }

    @Test
    void testDeleteCar() {
        Car car = new Car();
        car.setId("64d2a17acc1bdf7e9cac36a1");
        car.setName("TT");
        car.setModel(CarManufacturer.AUDI);
        car.setPrice(30000L);

        Mockito
            .when(service.deleteCar("64d2a17acc1bdf7e9cac36a1"))
            .thenReturn(Mono.just(car));

        webClient.delete().uri("/cars/{id}", "64d2a17acc1bdf7e9cac36a1")
            .exchange()
            .expectStatus().isOk();
    }
}