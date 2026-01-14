package com.skistation.reservationms.services;

import com.skistation.reservationms.dto.StudentDTO;
import com.skistation.reservationms.entities.Reservation;
import com.skistation.reservationms.repository.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private IReservationRepository reservationRepository;

    public Reservation addReservation(StudentDTO student) {
        Reservation res = new Reservation();
        if (student == null) {
            throw new IllegalArgumentException("Student does not exist.");
        }
        res.setStudentId(student.getId());
        res.setValid(true);
        res.setYearUniv("2024-2025"); // Set a default year
        return reservationRepository.save(res);
    }
}
