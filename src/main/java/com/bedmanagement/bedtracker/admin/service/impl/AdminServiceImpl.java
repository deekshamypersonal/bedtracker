package com.bedmanagement.bedtracker.admin.service.impl;

import com.bedmanagement.bedtracker.admin.ui.model.request.AllocationStatusModel;
import com.bedmanagement.bedtracker.admin.ui.model.request.HospitalRequestModel;
import com.bedmanagement.bedtracker.admin.ui.model.response.PatientResponseModel;
import com.bedmanagement.bedtracker.common.ApprovalStatus;
import com.bedmanagement.bedtracker.common.Constants;
import com.bedmanagement.bedtracker.exception.ErrorMessageConstant;
import com.bedmanagement.bedtracker.exception.UserServiceException;
import com.bedmanagement.bedtracker.io.entity.Hospital;
import com.bedmanagement.bedtracker.io.entity.Patient;
import com.bedmanagement.bedtracker.io.repository.HospitalRepository;
import com.bedmanagement.bedtracker.io.repository.PatientRepository;
import com.bedmanagement.bedtracker.workflow.WorkflowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Component
public class AdminServiceImpl {

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    WorkflowService workflowService;

    public String saveHospital(@RequestBody HospitalRequestModel hospitalRequestModel) {
        ModelMapper modelMapper = new ModelMapper();
        Hospital hospitalEntity = modelMapper.map(hospitalRequestModel, Hospital.class);
        try {
            hospitalRepository.save(hospitalEntity);
        }
        catch (DataIntegrityViolationException e) {
            throw new UserServiceException(ErrorMessageConstant.DUPLICATE_RECORD_ERROR_MSG);
        }
        catch(Exception e){
            throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
        }

        return Constants.SUCCESS_MSG;

    }

    public List<PatientResponseModel> getPendingPatientsList() {
        System.out.println("test");
        List<Patient> patientEntity;
        List<PatientResponseModel> patients;
        try {
            patientEntity = patientRepository.findPending(ApprovalStatus.PENDING);
            patients = new ArrayList<>();
            ModelMapper modelMapper = new ModelMapper();
            for (Patient patient : patientEntity) {
                patients.add(modelMapper.map(patient, PatientResponseModel.class));
            }
        }
        catch(RuntimeException e){
                throw new UserServiceException(ErrorMessageConstant.SERVER_ERROR_MSG);
            }
        return patients;
    }

    public byte[] downloadPrescription(String email) {
        Patient patient = patientRepository.findByEmail(email);
        return patient.getPrescriptionImage();
    }

    public String setStatus(AllocationStatusModel allocationStatusModel) {
        Patient patient=patientRepository.findByEmail(allocationStatusModel.getEmail());
        if(patient==null){
            throw new UserServiceException(ErrorMessageConstant.USER_NOT_FOUND);
        }
        workflowService.updatePatientStatus(allocationStatusModel,patient);
        return Constants.SUCCESS_MSG;
    }

    @Transactional
    public String setPatientArrivedStatus(String email) {
        Patient patient=patientRepository.findByEmail(email);
        if(patient==null){
            throw new UserServiceException(ErrorMessageConstant.USER_NOT_FOUND);
        }
        patient.setPatientArrived("Booked");
        patientRepository.save(patient);
        return Constants.SUCCESS_MSG;
    }
}
