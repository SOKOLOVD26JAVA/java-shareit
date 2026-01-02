package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import lombok.*;


/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Exclude
    private String name;

    @ToString.Include
    @EqualsAndHashCode.Exclude
    private String email;
}
