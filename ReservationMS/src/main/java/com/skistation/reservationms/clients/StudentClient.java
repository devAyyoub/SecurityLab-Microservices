package com.skistation.reservationms.clients;

import com.skistation.reservationms.dto.StudentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "studentms", 
    configuration = FeignClientConfigStudent.class
)
public interface StudentClient {

    @GetMapping("/students/{id}")
    StudentDTO getStudentById(@PathVariable Long id);
}