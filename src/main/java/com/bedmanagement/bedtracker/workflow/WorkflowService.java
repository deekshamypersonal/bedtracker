package com.bedmanagement.bedtracker.workflow;

import com.bedmanagement.bedtracker.admin.ui.model.request.AllocationStatusModel;
import com.bedmanagement.bedtracker.io.entity.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkflowService {

    @Autowired
    RejectionService rejectionService;

    @Autowired
    ApprovalService approvalService;


    public void updatePatientStatus(AllocationStatusModel allocationStatusModel, Patient patient){

        switch (allocationStatusModel.getStatus()) {
            case APPROVE:
                approvalService.approveRequest(allocationStatusModel,patient);
                break; // break is optional

            case REJECT:
                rejectionService.rejectRequest(allocationStatusModel,patient);
                break; // break is optional
        }
    }

    }



