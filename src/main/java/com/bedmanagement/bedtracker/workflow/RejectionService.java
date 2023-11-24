package com.bedmanagement.bedtracker.workflow;

import com.bedmanagement.bedtracker.admin.ui.model.request.AllocationStatusModel;
import com.bedmanagement.bedtracker.common.Constants;
import com.bedmanagement.bedtracker.io.entity.Patient;
import com.bedmanagement.bedtracker.io.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class RejectionService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void rejectRequest(AllocationStatusModel allocationStatusModel, Patient patient){
        //delete record from patient
        patientRepository.delete(patient);
        //send email
        sendEmail(allocationStatusModel.getEmail(),allocationStatusModel.getReason());
    }

    public void sendEmail(String email,String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constants.SYSTEM_EMAIL);
        message.setTo(email);
        message.setSubject("Bed Request Rejected");
        message.setText("Your Bed Request has been Rejected.Reason:"+reason);
        mailSender.send(message);

    }
}
