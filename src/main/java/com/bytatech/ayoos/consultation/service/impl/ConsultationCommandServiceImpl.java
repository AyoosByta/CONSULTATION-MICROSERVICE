package com.bytatech.ayoos.consultation.service.impl;

import java.util.ArrayList; 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bytatech.ayoos.consultation.client.SMS.SMSResourceApiIN;
import com.bytatech.ayoos.consultation.client.SMS.SMSResourceApiUK;
import com.bytatech.ayoos.consultation.client.activiti.api.FormsApi;
import com.bytatech.ayoos.consultation.client.activiti.api.ProcessInstancesApi;
import com.bytatech.ayoos.consultation.client.activiti.model.ProcessInstanceCreateRequest;
import com.bytatech.ayoos.consultation.client.activiti.model.ProcessInstanceResponse;
import com.bytatech.ayoos.consultation.client.activiti.model.RestFormProperty;
import com.bytatech.ayoos.consultation.client.activiti.model.RestVariable;
import com.bytatech.ayoos.consultation.client.activiti.model.SubmitFormRequest;
import com.bytatech.ayoos.consultation.domain.BasicCheckUp;
import com.bytatech.ayoos.consultation.domain.OTPChallenge;
import com.bytatech.ayoos.consultation.domain.OTPResponse;
import com.bytatech.ayoos.consultation.repository.BasicCheckUpRepository;
import com.bytatech.ayoos.consultation.resource.assembler.NextTaskAssembler;
import com.bytatech.ayoos.consultation.resource.assembler.NextTaskResource;
import com.bytatech.ayoos.consultation.service.ConsultationCommandService;
 
 
@Service
@Transactional
public class ConsultationCommandServiceImpl implements ConsultationCommandService {
	private final Logger log = LoggerFactory.getLogger(ConsultationCommandServiceImpl.class);
	@Autowired
	private SMSResourceApiUK smsResourceApiUK;
	@Autowired
	private SMSResourceApiIN smsResourceApiIN;
	
	
	@Value("${smsgateway.credentials.in-apiKey}")
	private String apiKey_IN;

	@Value("${smsgateway.in-sender}")
	private String SMSsender_IN;

	@Value("${smsgateway.credentials.uk-apiKey}")
	private String apiKey_UK;

	@Value("${smsgateway.uk-sender}")
	private String SMSsender_UK;
    
	@Autowired
    private ProcessInstancesApi processInstanceApi;
    @Autowired
	private NextTaskAssembler resourceAssembler;
    @Autowired
    private FormsApi formsApi;
    @Autowired
    private  BasicCheckUpRepository basicCheckUpRepository;
    @Override
    public NextTaskResource initiate() {
    	log .info("into ====================initiate()");
		ProcessInstanceCreateRequest processInstanceCreateRequest=new ProcessInstanceCreateRequest();
   		 List<RestVariable> variables=new ArrayList<RestVariable>(); 
   		processInstanceCreateRequest.setProcessDefinitionId("ConsultationNew:1:30006");
   		log.info("*****************************************************"+processInstanceCreateRequest.getProcessDefinitionId());
   		RestVariable driverRestVariable=new RestVariable();
   		driverRestVariable.setName("doctor");
   		driverRestVariable.setType("string");
   		driverRestVariable.setValue("doctor");
   		RestVariable driverRestVariable2=new RestVariable();
   		driverRestVariable2.setName("patient");
   		driverRestVariable2.setType("string");
   		driverRestVariable2.setValue("patient");
   		variables.add(driverRestVariable);
   		variables.add(driverRestVariable2);
   		processInstanceCreateRequest.setVariables(variables);
   		ResponseEntity<ProcessInstanceResponse> processInstanceResponse = processInstanceApi.createProcessInstance(processInstanceCreateRequest);
		String processInstanceId = processInstanceResponse.getBody().getId();
		String processDefinitionId = processInstanceCreateRequest.getProcessDefinitionId();
		log.info("++++++++++++++++processDefinitionId++++++++++++++++++"+ processDefinitionId);
		log.info("++++++++++++++++ProcessInstanceId is+++++++++++++ " + processInstanceId);
		
   		processInstanceApi.createProcessInstance(processInstanceCreateRequest);
   		NextTaskResource nextTaskResource = resourceAssembler.toResource(processInstanceId);
		 
		return nextTaskResource;
	}
    @Override
    public NextTaskResource basicCheckUpTask(String processId, BasicCheckUp basicCheckUp) {
 
        log .info("into ====================basicCheckUpTask()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		NextTaskResource nextTaskResource = resourceAssembler.toResource(processId);
   		submitFormRequest.setTaskId(nextTaskResource.getNextTaskId());
   		 
   		  
   		RestFormProperty checkUpStatusFormProperty = new RestFormProperty();
   		checkUpStatusFormProperty.setId("checkUpStatus");
   		checkUpStatusFormProperty.setName("checkUpStatus");
   		checkUpStatusFormProperty.setType("String");
   		checkUpStatusFormProperty.setReadable(true);
   		checkUpStatusFormProperty.setValue((basicCheckUp.getCheckUpStatus()) );
   		formProperties.add(checkUpStatusFormProperty);
   		
   		 submitFormRequest.setProperties(formProperties); 
   		formsApi.submitForm(submitFormRequest);
   		if(basicCheckUp.getCheckUpStatus().equals("Required"))
   		basicCheckUpRepository.save(basicCheckUp);
		System.out.println("Task id=##"+nextTaskResource.getNextTaskId());
		nextTaskResource = resourceAssembler.toResource(processId);
		return nextTaskResource;
  	}
	@Override
	public NextTaskResource accessPatientHistory(String processId, String choice) {
		log .info("into ====================accessPatientHistory()");
   		List<RestFormProperty>formProperties=new ArrayList<RestFormProperty>();
   		SubmitFormRequest submitFormRequest = new SubmitFormRequest();
   		submitFormRequest.setAction("completed");
   		NextTaskResource nextTaskResource = resourceAssembler.toResource(processId);
   		submitFormRequest.setTaskId(nextTaskResource.getNextTaskId());
   		RestFormProperty bpFormProperty = new RestFormProperty();
   		bpFormProperty.setId("choice");
   		bpFormProperty.setName("choice");
   		bpFormProperty.setType("String");
   		bpFormProperty.setReadable(true);
   		bpFormProperty.setValue(choice);
   		formProperties.add(bpFormProperty);
   		
   	 submitFormRequest.setProperties(formProperties); 
		formsApi.submitForm(submitFormRequest);
		
		if(choice.equals("Accept")) {
			//otp message send
			Long number=919633470727L;
			 OTPResponse res= sendSMS(number);
			 
		}
		
		nextTaskResource = resourceAssembler.toResource(processId);
		return nextTaskResource;
	}
	@Override
	public NextTaskResource consultPatient(String processId ) {
		 
		return null;
	}
	@Override
	public NextTaskResource patientPrescription(String processId) {
		 
		return null;
	} 
	
	public OTPResponse sendSMS(Long numbers) {
		if (numbers.toString().substring(0, 2).equals("91")) {
			log.info("it is an indian number");
			String message = "Dear User, Enter your OTP to complete registration. OTP to verify your Mobile is ";
			return smsResourceApiIN.sendSMS(message, apiKey_IN, numbers, SMSsender_IN);
		} else {
			log.info("it is not an indian number");
			String message = "Dear User, Enter your OTP to complete registration. OTP to verify your Mobile is ";
			return smsResourceApiUK.sendSMS(message, apiKey_UK, numbers, SMSsender_UK);
		}

	}
 
	public OTPChallenge verifyOTP(Long numbers, String code) {
		if (numbers.toString().substring(0, 2).equals("91")) {
			return smsResourceApiIN.verifyOTP(numbers, code, apiKey_IN);
		} else {
			return smsResourceApiUK.verifyOTP(numbers, code, apiKey_UK);

		}
	}
}
