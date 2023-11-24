package com.bedmanagement.bedtracker.user.ui.controller;

import com.bedmanagement.bedtracker.common.Utility;
import com.bedmanagement.bedtracker.filestorage.FileCloudStorage;
import com.bedmanagement.bedtracker.io.repository.UserRepository;
import com.bedmanagement.bedtracker.user.service.impl.UserServiceImpl;
import com.bedmanagement.bedtracker.user.ui.model.request.PatientForm;
import com.bedmanagement.bedtracker.user.ui.model.request.UserRegister;
import com.bedmanagement.bedtracker.user.ui.model.response.HospitalResponseModel;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bedtracker")
public class UserController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileCloudStorage fileCloudStorage;

    public UserController() throws IOException {
    }

    @PostMapping(path = "/register1")
    String test() throws IOException {
       // fileCloudStorage.uploadFile(file.getBytes(),"dt");
String x="abc";


        return "test";
    }
    @PostMapping(path = "/register")
    ResponseEntity<String> registerUser(@Valid @RequestBody UserRegister userRegister) {
        String message = userServiceImpl.registerUser(userRegister);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }
    @PostMapping(path = "/fillform")
    ResponseEntity<String> fillForm(@Valid @RequestBody PatientForm patientForm) {
        String message = userServiceImpl.fillForm(patientForm);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }
    @GetMapping(path = "/getHospital/{city}")
    ResponseEntity<List<HospitalResponseModel>> fillForm(@PathVariable String city) {
        return new ResponseEntity<>(userServiceImpl.getAllHospitals(city), new HttpHeaders(), HttpStatus.OK);
    }
    @PostMapping(path = "/uploadPrescription")
    ResponseEntity<String> uploadPrescription(@RequestParam("file") MultipartFile file) {
        Utility.validateFiles(file);
        String message = userServiceImpl.uploadPrescription(file);
        return new ResponseEntity<>(message, new HttpHeaders(), HttpStatus.OK);
    }
}
