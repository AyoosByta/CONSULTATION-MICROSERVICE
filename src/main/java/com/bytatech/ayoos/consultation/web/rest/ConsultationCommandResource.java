package com.bytatech.ayoos.consultation.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bytatech.ayoos.consultation.domain.BasicCheckUp;
import com.bytatech.ayoos.consultation.domain.Consultation;
import com.bytatech.ayoos.consultation.resource.assembler.NextTaskResource;
import com.bytatech.ayoos.consultation.service.ConsultationCommandService; 
 

@RestController
@RequestMapping("/api")
public class ConsultationCommandResource {
    @Autowired
	private final ConsultationCommandService consultationCommandService;
    public ConsultationCommandResource(ConsultationCommandService consultationCommandService) {
        this.consultationCommandService = consultationCommandService;
    }
	@PostMapping("/initiate")
	public NextTaskResource initiateConsultation() {
		NextTaskResource task=consultationCommandService.initiate();
		return task;
	}
	@PostMapping("/basicCheckup/{processId}")
	public NextTaskResource basicCheckUpTask(@PathVariable String processId, @RequestBody BasicCheckUp basicCheckUp) {
		NextTaskResource task=consultationCommandService.basicCheckUpTask(processId,basicCheckUp);
		return task;
	}
	@PostMapping("/accessPatientHistory/{processId}")
	public NextTaskResource accessPatientHistory(@PathVariable String processId, @RequestBody String choice) {
		NextTaskResource task=consultationCommandService.accessPatientHistory(processId,choice);
		return task;
	}
	@PostMapping("/consultPatient/{processId}")
	public NextTaskResource consultPatient(@PathVariable String processId, @RequestBody Consultation consultation) {
		NextTaskResource task=consultationCommandService.consultPatient(processId );
		return task;
	}
	@PostMapping("/patientPrescription/{processId}")
	public NextTaskResource patientPrescription(@PathVariable String processId, @RequestBody String otp) {
		NextTaskResource task=consultationCommandService.patientPrescription(processId );
		return task;
	}
}
