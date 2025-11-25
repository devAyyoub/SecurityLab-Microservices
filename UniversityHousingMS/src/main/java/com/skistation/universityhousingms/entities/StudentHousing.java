package com.skistation.universityhousingms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentHousing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHousing;
    private String name;
    private long capacity;

    @ManyToOne
    @JoinColumn(name = "bloc_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bloc bloc;

    @OneToOne
    @JoinColumn(name = "university_id", unique = true)
    private University university;
}
