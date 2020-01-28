package com.bytatech.ayoos.consultation.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bytatech.ayoos.consultation.service.ConsultationCommandService; 
 

@RestController
@RequestMapping("/api/")
public class ConsultationCommandResource {
    @Autowired
	private final ConsultationCommandService consultationCommandService;
    public ConsultationCommandResource(ConsultationCommandService consultationCommandService) {
        this.consultationCommandService = consultationCommandService;
    }
	@PostMapping("/initiate")
	public String initiateConsultation() {
		String s=consultationCommandService.initiate();
		return s;
	}
}
