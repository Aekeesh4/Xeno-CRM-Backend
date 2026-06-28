package com.xeno.xenocrm.controller;

import com.xeno.xenocrm.entity.Lead;
import com.xeno.xenocrm.repository.LeadRepository;
import com.xeno.xenocrm.service.AIService;
import com.xeno.xenocrm.service.GeminiService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private LeadRepository leadRepository;


    // ===========================
    // AI Email Generator
    // ===========================

    @GetMapping(
            value = "/email/{id}",
            produces = "text/plain"
    )
    public String generateEmail(
            @PathVariable Long id
    ) {

        Lead lead = leadRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Lead Not Found"));

        return aiService.generateAIEmail(lead);
    }


    // ===========================
    // Rule Based Campaign
    // ===========================

    @PostMapping("/campaign")
    public String generateCampaign(
            @RequestBody Map<String, String> request
    ) {

        return aiService.generateCampaign(
                request.get("prompt")
        );
    }


    // ===========================
    // Gemini AI Campaign
    // ===========================

    @PostMapping("/gemini")
    public String generateWithGemini(
            @RequestBody Map<String, String> request
    ) {

        String prompt = request.get("prompt");

        return geminiService.generateContent(prompt);
    }
    @PostMapping("/chat")
    public String chatWithAI(

            @RequestBody Map<String, String> request

    ) {
        System.out.println("Question Received = " + request.get("question"));
        return aiService.chatWithAI(

                request.get("question")

        );

    }

}