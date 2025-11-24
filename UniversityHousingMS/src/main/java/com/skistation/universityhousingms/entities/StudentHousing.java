package com.skistation.universityhousingms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
