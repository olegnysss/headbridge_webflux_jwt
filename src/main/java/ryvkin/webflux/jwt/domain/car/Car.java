package ryvkin.webflux.jwt.domain.car;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@EqualsAndHashCode(of = {"id","name"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(value = "cars")
public class Car {
    @Id
    private String id;
    private String name;
    private CarManufacturer model;
    private Long price;
}
