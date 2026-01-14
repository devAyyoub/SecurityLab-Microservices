package com.skistation.reservationms.controller;

import com.skistation.reservationms.entities.Reservation;
import com.skistation.reservationms.repository.IReservationRepository;
import com.skistation.reservationms.clients.StudentClient;
import com.skistation.reservationms.dto.StudentDTO;
import com.skistation.reservationms.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/reservations")
public class ReservationController {


    @Autowired
    ReservationService reservationService;

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private StudentClient studentClient;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    // Create reservation with student validation
    @PostMapping(params = "studentId")
    @PreAuthorize("hasAuthority('ROLE_STUDENT.READ')")
    public ResponseEntity<Reservation> createReservation(@RequestParam("studentId") Long studentId) {
        try {
            StudentDTO student = studentClient.getStudentById(studentId);
            if (student == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID " + studentId);
            }
            Reservation reservation = reservationService.addReservation(student);
            return new ResponseEntity<>(reservation, HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with ID " + studentId, e);
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied when calling Student service. Check OAuth2 token and roles.", e);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error from Student service: " + e.getMessage(), e);
            }
        } catch (org.springframework.web.client.ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Cannot connect to Student service. Is it running? Error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error communicating with Student service: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    // Read all
    @GetMapping
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        reservationRepository.findAll().forEach(list::add);
        return list;
    }

    // Read by id
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> r = reservationRepository.findById(id);
        return r.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get student info for a reservation
    @GetMapping("/{id}/student")
    public ResponseEntity<StudentDTO> getStudentForReservation(@PathVariable Long id) {
        Optional<Reservation> r = reservationRepository.findById(id);
        if (r.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation reservation = r.get();
        Long studentId = reservation.getStudentId();
        if (studentId == null) {
            return ResponseEntity.badRequest().build();
        }
        StudentDTO student = null;
        try {
            student = studentClient.getStudentById(studentId);
        } catch (Exception e) {
            // If the downstream call fails, return 502 Bad Gateway
            return ResponseEntity.status(502).build();
        }
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        Optional<Reservation> existing = reservationRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Reservation toUpdate = existing.get();
        toUpdate.setYearUniv(reservation.getYearUniv());
        toUpdate.setValid(reservation.isValid());
        Reservation saved = reservationRepository.save(toUpdate);
        return ResponseEntity.ok(saved);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Optional<Reservation> existing = reservationRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
