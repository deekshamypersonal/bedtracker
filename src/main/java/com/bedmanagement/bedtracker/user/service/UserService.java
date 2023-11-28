package com.bedmanagement.bedtracker.user.service;

import com.bedmanagement.bedtracker.user.ui.model.request.PasswordResetModel;
import com.bedmanagement.bedtracker.user.ui.model.request.PatientForm;
import com.bedmanagement.bedtracker.user.ui.model.request.UserRegister;
import com.bedmanagement.bedtracker.user.ui.model.response.HospitalResponseModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    public String registerUser(UserRegister userRegister);

    String verifyEmailToken(String token);

    public String fillForm(PatientForm patientForm);
    public List<HospitalResponseModel> getAllHospitals(String city);
    public String uploadPrescription(MultipartFile file);


    String requestPasswordReset(String email);

    String resetPassword(String token, PasswordResetModel passwordResetModel);
}
