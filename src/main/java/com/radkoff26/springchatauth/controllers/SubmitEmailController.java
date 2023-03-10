package com.radkoff26.springchatauth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.radkoff26.springchatauth.domain.body.request.EmailSubmissionBody;
import com.radkoff26.springchatauth.services.declaration.email.RequestEmailSubmissionService;
import com.radkoff26.springchatauth.services.declaration.email.SubmitEmailService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class SubmitEmailController {
    private final RequestEmailSubmissionService requestEmailSubmissionService;
    private final SubmitEmailService submitEmailService;

    public SubmitEmailController(RequestEmailSubmissionService requestEmailSubmissionService, SubmitEmailService submitEmailService) {
        this.requestEmailSubmissionService = requestEmailSubmissionService;
        this.submitEmailService = submitEmailService;
    }

    @PostMapping("/request")
    public ResponseEntity<Void> requestEmailSubmission(@RequestParam("userId") long userId) {
        log.info("Requested email submission from user {}", userId);
        ResponseEntity<Void> response = requestEmailSubmissionService.requestEmailSubmission(userId);
        log.info("Requested email submission from user {}, got status {}", userId, response.getStatusCode().value());
        return response;
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitEmail(@RequestBody EmailSubmissionBody emailSubmissionBody) {
        return submitEmailService.submitEmail(emailSubmissionBody);
    }
}
