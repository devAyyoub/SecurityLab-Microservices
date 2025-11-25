package com.skistation.universityhousingms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUniversity;
    private String name;
    private String address;

    @OneToOne(mappedBy = "university", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private StudentHousing studentHousing;
}
