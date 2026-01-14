package com.skistation.reservationms.services;

import com.skistation.reservationms.clients.Student;
import com.skistation.reservationms.entities.Reservation;
import com.skistation.reservationms.repository.IReservationRepository;

public class IReservationService  {

    IReservationRepository reservationRepository ;
    public Reservation addReservation(Student student) {
        Reservation res = new Reservation();
        if (student == null) {
            throw new IllegalArgumentException("Student does not exist.");
        }
        res.setStudentId(student.getIdStudent());
//        res.setStartDate(LocalDate.now());
//        res.setEndDate(LocalDate.now().plusYears(1));
        res.setValid(true);
        return  reservationRepository.save(res);  }
}
