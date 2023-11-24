package com.bedmanagement.bedtracker.workflow;

import com.bedmanagement.bedtracker.admin.ui.model.request.AllocationStatusModel;
import com.bedmanagement.bedtracker.common.ApprovalStatus;
import com.bedmanagement.bedtracker.common.Constants;
import com.bedmanagement.bedtracker.filestorage.FileCloudStorage;
import com.bedmanagement.bedtracker.io.entity.Hospital;
import com.bedmanagement.bedtracker.io.entity.Patient;
import com.bedmanagement.bedtracker.io.repository.PatientRepository;
import com.bedmanagement.bedtracker.schedular.TaskSchedular;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ApprovalService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    FileCloudStorage fileCloudStorage;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    TaskSchedular myTaskScheduler;

    public void approveRequest(AllocationStatusModel allocationStatusModel, Patient patient){
        //UUID generation

        UUID uuid = UUID.randomUUID();

        patient.setUniqueId(uuid);

        //status->apprival

        patient.setHosptialAllocatedStatus(ApprovalStatus.APPROVE);
        byte[] prescription=patient.getPrescriptionImage();
        patient.setPrescriptionImage(null);
        patient.setEndDate(patient.getStartDate().plusDays(patient.getDaysRequired()));

        //end date update

        Hospital hospital=patient.getHospital();
        Integer noOfBedsAvailable=hospital.getNoOfBedsAvailable();
        noOfBedsAvailable--;
        hospital.setNoOfBedsAvailable(noOfBedsAvailable);

        //sae to db

        patientRepository.save(patient);

        //save prescription to cloud
if(prescription!=null)
        fileCloudStorage.uploadFile(prescription,allocationStatusModel.getEmail());

        //email
        sendEmail(uuid.toString(),allocationStatusModel.getEmail());


        //task schedult to cancel booking after days required
        myTaskScheduler.scheduleATask(LocalDate.now().atTime(8, 0),allocationStatusModel.getEmail(), "patient didn't arrive before 8 pm",false);
        myTaskScheduler.scheduleATask(LocalDateTime.now().plusDays(3),allocationStatusModel.getEmail(), "patient checkout time has passed",true);
    }

        public void sendEmail(String uuid, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constants.SYSTEM_EMAIL);
        message.setTo(email);
        message.setSubject("Bed Request Approved");
        message.setText("Your Bed Request has been Approved. Your identification number is"+uuid+
                "Please bring identity proof and show this mail at hospital desk");
        mailSender.send(message);

    }


}
