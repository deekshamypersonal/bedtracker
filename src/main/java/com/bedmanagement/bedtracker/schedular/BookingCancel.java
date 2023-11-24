package com.bedmanagement.bedtracker.schedular;

import com.bedmanagement.bedtracker.SpringApplicationContext;
import com.bedmanagement.bedtracker.filestorage.FileCloudStorage;
import com.bedmanagement.bedtracker.io.entity.Hospital;
import com.bedmanagement.bedtracker.io.entity.Patient;
import com.bedmanagement.bedtracker.io.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class BookingCancel implements Runnable{

    @Autowired
    private JavaMailSender mailSender;


//    @Autowired
//    PatientRepository patientRepository;

//    @Autowired
//    FileCloudStorage fileCloudStorage;

    String email;
    String reason;
    Boolean deleteFromCloud;
    BookingCancel(String email,String reason,Boolean deleteFromCloud){
        this.email=email;
        this.reason=reason;
        this.deleteFromCloud=deleteFromCloud;
    }


    @Override
    public void run() {

      //  delete patient entity
        PatientRepository patientRepository=(PatientRepository)SpringApplicationContext.getBean("patientRepository");

        FileCloudStorage fileCloudStorage=(FileCloudStorage)SpringApplicationContext.getBean("fileCloudStorage");
        Patient patient= patientRepository.findByEmail(email);

        if(patient.getPatientArrived()=="Booked" && deleteFromCloud==false){
            return;
        }
        if(deleteFromCloud){
            fileCloudStorage.deleteFile(email);
        }

        Hospital hospital=patient.getHospital();
        Integer noOfBedsAvailable=hospital.getNoOfBedsAvailable();
        noOfBedsAvailable++;
        hospital.setNoOfBedsAvailable(noOfBedsAvailable);

        patientRepository.delete(patient);

        sendEmail(email,reason);

    }

    public void sendEmail(String email,String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("deekshautrip@gmail.com");
        message.setTo(email);
        message.setSubject("Booking Cancelled");
        message.setText("Your booking has been cancelled. Rebook if needed.Reason: "+reason);
        mailSender.send(message);

    }




}
