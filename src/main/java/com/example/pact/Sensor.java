package com.example.pact;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sensor {

    @Id
    @GeneratedValue
    private int id;
    private String name;
}
