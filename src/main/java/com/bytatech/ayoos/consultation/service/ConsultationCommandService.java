package com.bytatech.ayoos.consultation.service;
  

import com.bytatech.ayoos.consultation.domain.BasicCheckUp;  
import com.bytatech.ayoos.consultation.resource.assembler.NextTaskResource;

public interface ConsultationCommandService {
	NextTaskResource initiate(); 

	NextTaskResource basicCheckUpTask(String processId, BasicCheckUp basicCheckUp);

	NextTaskResource accessPatientHistory(String processId, String choice);
	NextTaskResource consultPatient( String processId );

	NextTaskResource patientPrescription(String processId);
}
