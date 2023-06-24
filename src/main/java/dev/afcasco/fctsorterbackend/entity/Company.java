package dev.afcasco.fctsorterbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cif;
    private String name;
    private String address;
    private String city;
    private String zipCode;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Status status;


}
