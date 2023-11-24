package com.bedmanagement.bedtracker.admin.ui.controller;

import com.bedmanagement.bedtracker.admin.service.impl.AdminServiceImpl;
import com.bedmanagement.bedtracker.admin.ui.model.request.AllocationStatusModel;
import com.bedmanagement.bedtracker.admin.ui.model.request.HospitalRequestModel;
import com.bedmanagement.bedtracker.admin.ui.model.response.PatientResponseModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    public static final String FILE_CONTENT_TYPE = "application/octet-stream";

    @Autowired
    AdminServiceImpl adminServiceImpl;


    @PostMapping("/save_hospital")
    ResponseEntity<String> saveHospital(@Valid @RequestBody HospitalRequestModel HospitalRequestModel){
        String message=adminServiceImpl.saveHospital(HospitalRequestModel);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/getPatients")
    ResponseEntity<List<PatientResponseModel>> getPendingPatientsList(){
        return new ResponseEntity<>(adminServiceImpl.getPendingPatientsList(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/downloadPrescription/{email}")
    ResponseEntity<InputStreamResource> downloadPrescription(@PathVariable("email") String email){
        byte[] prescription=adminServiceImpl.downloadPrescription(email);
        String headerValue = "attachment; filename=\"" + "file" + "\"";
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(prescription));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(FILE_CONTENT_TYPE))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    @GetMapping("/getPendingPatients")
    List<PatientResponseModel> getAllocationStatus(){
        return adminServiceImpl.getPendingPatientsList();
    }

    @PostMapping("/setApproval")
    ResponseEntity<String> setApprovalStatus(@Valid @RequestBody AllocationStatusModel allocationStatusModel){
        String message=adminServiceImpl.setStatus(allocationStatusModel);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("/patientArrived/{email}")
    ResponseEntity<String> setPatientArrivedStatus(@PathVariable("email") String email){
        String message=adminServiceImpl.setPatientArrivedStatus(email);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }

}
