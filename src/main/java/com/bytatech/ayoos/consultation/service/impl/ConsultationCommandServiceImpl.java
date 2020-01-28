package com.bytatech.ayoos.consultation.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytatech.ayoos.consultation.client.activiti.api.FormsApi;
import com.bytatech.ayoos.consultation.client.activiti.api.ProcessInstancesApi;
import com.bytatech.ayoos.consultation.client.activiti.model.ProcessInstanceCreateRequest;
import com.bytatech.ayoos.consultation.client.activiti.model.ProcessInstanceResponse;
import com.bytatech.ayoos.consultation.client.activiti.model.RestVariable;
import com.bytatech.ayoos.consultation.service.ConsultationCommandService;
 
 
@Service
@Transactional
public class ConsultationCommandServiceImpl implements ConsultationCommandService {
	private final Logger log = LoggerFactory.getLogger(ConsultationCommandServiceImpl.class);
    @Autowired
    private ProcessInstancesApi processInstanceApi;
    @Autowired
    private FormsApi formsApi;
    public String initiate() {
		ProcessInstanceCreateRequest processInstanceCreateRequest=new ProcessInstanceCreateRequest();
   		 List<RestVariable> variables=new ArrayList<RestVariable>(); 
   		processInstanceCreateRequest.setProcessDefinitionId("ConsultationNew:1:30006");
   		log.info("*****************************************************"+processInstanceCreateRequest.getProcessDefinitionId());
   		RestVariable driverRestVariable=new RestVariable();
   		driverRestVariable.setName("doctor");
   		driverRestVariable.setType("string");
   		driverRestVariable.setValue("doctor");
   		RestVariable driverRestVariable2=new RestVariable();
   		driverRestVariable2.setName("doctor");
   		driverRestVariable2.setType("string");
   		driverRestVariable2.setValue("doctor");
   		variables.add(driverRestVariable);
   		variables.add(driverRestVariable2);
   		processInstanceCreateRequest.setVariables(variables);
   		ResponseEntity<ProcessInstanceResponse> processInstanceResponse = processInstanceApi.createProcessInstance(processInstanceCreateRequest);
		String processInstanceId = processInstanceResponse.getBody().getId();
		String processDefinitionId = processInstanceCreateRequest.getProcessDefinitionId();
		log.info("++++++++++++++++processDefinitionId++++++++++++++++++"+ processDefinitionId);
		log.info("++++++++++++++++ProcessInstanceId is+++++++++++++ " + processInstanceId);
		
   		processInstanceApi.createProcessInstance(processInstanceCreateRequest);
   		
		return processInstanceId;
	}
}
