package com.bedmanagement.bedtracker.user.service.impl;

import com.bedmanagement.bedtracker.common.ApprovalStatus;
import com.bedmanagement.bedtracker.common.Constants;
import com.bedmanagement.bedtracker.common.Utility;
import com.bedmanagement.bedtracker.exception.ErrorMessageConstant;
import com.bedmanagement.bedtracker.exception.UserServiceException;
import com.bedmanagement.bedtracker.io.entity.Hospital;
import com.bedmanagement.bedtracker.io.entity.Patient;
import com.bedmanagement.bedtracker.io.entity.User;
import com.bedmanagement.bedtracker.io.repository.HospitalRepository;
import com.bedmanagement.bedtracker.io.repository.PatientRepository;
import com.bedmanagement.bedtracker.io.repository.UserRepository;
import com.bedmanagement.bedtracker.security.EmailVerificationService;
import com.bedmanagement.bedtracker.user.service.UserService;
import com.bedmanagement.bedtracker.user.ui.controller.UserController;
import com.bedmanagement.bedtracker.user.ui.model.request.PasswordResetModel;
import com.bedmanagement.bedtracker.user.ui.model.request.PatientForm;
import com.bedmanagement.bedtracker.user.ui.model.request.UserRegister;
import com.bedmanagement.bedtracker.user.ui.model.response.HospitalResponseModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

//    @Autowired
//    ProducerService producerService;

    @Autowired
    EmailVerificationService emailVerificationService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    public String registerUser(UserRegister userRegister) {
        logger.info("Method registerUser invoked for user");
        ModelMapper modelMapper = new ModelMapper();
        User userEntity = modelMapper.map(userRegister, User.class);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));
        userEntity.setRole("ROLE_USER");
        String token = Utility.generateToken(userRegister.getEmail());
        userEntity.setEmailVerificationToken(token);
        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new UserServiceException(ErrorMessageConstant.DUPLICATE_RECORD_ERROR_MSG);
        } catch (Exception e) {
            throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
        }
        emailVerificationService.sendRegisterUserMail(userRegister.getEmail(), token);

        return "Please check Email to verify";
    }

    @Override
    public String verifyEmailToken(String token) {
        logger.info("Method verifyEmailToken invoked for user");
        boolean isTokenValid = false;

        // Find user by token
        User userEntity = userRepository.findUserByEmailVerificationToken(token);
        if (userEntity != null) {
            isTokenValid = Utility.isTokenValid(token);
            if (isTokenValid) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
            }
        }

        return isTokenValid?"Email successfully Verifies":"Email Validation Unsuccessful";
    }

    public String fillForm(PatientForm patientForm) {
        logger.info("Method fillForm invoked for user");
        ModelMapper modelMapper = new ModelMapper();
        Patient patient = modelMapper.map(patientForm, Patient.class);
        patient.setHosptialAllocatedStatus(ApprovalStatus.PENDING);
        String currentPrincipalName = getLoggedInUser();
        User user = userRepository.findByEmail(currentPrincipalName);
        Optional<Hospital> hospital = hospitalRepository.findById(patientForm.getHospital_id());
        if (hospital.isEmpty()) {
            throw new UserServiceException(ErrorMessageConstant.HOSPITAL_NOT_FOUND);
        }
        User currentUser = new User();
        currentUser.setUser_id(user.getUser_id());
        patient.setUser(currentUser);
        patient.setHospital(hospital.get());
        String subject = "A bed Request has been made for: " + currentPrincipalName;
        sendEmailToAdmin(Constants.ADMIN_EMAIL, "Bed Request Received",
                subject);
        // producerService.buildAndSendMail(Constants.ADMIN_EMAIL,"Bed Request Received",subject);
        patient.setEmail(currentPrincipalName);
        patient.setStartDate(LocalDateTime.now());
        try {
            patientRepository.save(patient);
        } catch (RuntimeException e) {
            throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
        }

        return Constants.SUCCESS_MSG + " Please Check Mail.";
    }

    public List<HospitalResponseModel> getAllHospitals(String city) {
        logger.info("Method getAllHospitals invoked for user");
        List<Hospital> hospitalEntities = hospitalRepository.fillAllByCity(city);
        List<HospitalResponseModel> hospitals = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Hospital hospital : hospitalEntities) {
            hospitals.add(modelMapper.map(hospital, HospitalResponseModel.class));
        }
        return hospitals;


    }

    public String uploadPrescription(MultipartFile file) {
        logger.info("Method uploadPrescription invoked for user");

        String loggedInUser = getLoggedInUser();
        Patient patient = patientRepository.findByEmail(loggedInUser);
        try {
            patient.setPrescriptionImage(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        patientRepository.save(patient);
        return Constants.SUCCESS_MSG;
    }

    @Override
    public String requestPasswordReset(String email) {
        logger.info("Method requestPasswordReset invoked for user");
        User user=userRepository.findByEmail(email);
        if(user==null)
            throw new UserServiceException("No such email exists in our record. Please recheck");
        String token = Utility.generateToken(email);
        user.setEmailVerificationToken(token);
        userRepository.save(user);
        emailVerificationService.sendPaswordResetMail(email, token);
        return Constants.SUCCESS_MSG + " Please Check Mail.";

    }

    @Override
    public String resetPassword(String token, PasswordResetModel passwordResetModel) {

        boolean isTokenValid = false;

        // Find user by token
        User userEntity = userRepository.findUserByEmailVerificationToken(token);
        if (userEntity != null) {
            isTokenValid = Utility.isTokenValid(token);
            if (isTokenValid && passwordResetModel.getNewPassword().equals(passwordResetModel.getConfirmationPassword())) {
                userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(passwordResetModel.getNewPassword()));
                userRepository.save(userEntity);
            }
        }

        return isTokenValid?"Password Reset Successful":"Password Reset Unsuccessful";

    }

    public void sendEmailToAdmin(String email, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constants.SYSTEM_EMAIL);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(msg);
        mailSender.send(message);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        {
            User userEntity = userRepository.findByEmail(email);
            if (userEntity == null)
                throw new UsernameNotFoundException(email);
            List<GrantedAuthority> authorities = getGrantedAuthorities(userEntity);
            return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                    userEntity.getEmailVerificationStatus(),
                    true, true,
                    true, authorities);
        }
    }

    public static List<GrantedAuthority> getGrantedAuthorities(User userEntity) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRole()));
        return authorities;
    }

    private static String getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
