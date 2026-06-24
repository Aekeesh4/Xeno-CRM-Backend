package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.Lead;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    public int calculateLeadScore(Lead lead) {

        int score = 0;

        // Source Score
        if (lead.getSource() != null) {

            String source =
                    lead.getSource().toLowerCase();

            if (source.contains("referral")) {

                score += 30;

            } else if (source.contains("linkedin")) {

                score += 25;

            } else if (source.contains("website")) {

                score += 20;

            } else {

                score += 10;

            }
        }


        // Company Score

        if (lead.getCompany() != null
                && !lead.getCompany().isBlank()) {

            score += 20;

        }


        // Notes Score

        if (lead.getNotes() != null
                && lead.getNotes().length() > 50) {

            score += 20;

        }

        else if (lead.getNotes() != null
                && lead.getNotes().length() > 20) {

            score += 10;

        }


        // Email Score

        if (lead.getEmail() != null
                && !lead.getEmail().isBlank()) {

            String email =
                    lead.getEmail().toLowerCase();

            if (email.endsWith("@gmail.com")) {

                score += 10;

            }

            else {

                score += 25;

            }

        }


        // Phone Score

        if (lead.getPhone() != null
                && !lead.getPhone().isBlank()) {

            score += 15;

        }


        return Math.min(score, 100);

    }



    public String getPriority(int score) {

        if (score >= 80) {

            return "HOT LEAD 🔥";

        }

        else if (score >= 50) {

            return "WARM LEAD ⭐";

        }

        else {

            return "COLD LEAD ❄";

        }

    }



    public String generateSummary(Lead lead) {

        StringBuilder summary =
                new StringBuilder();

        summary.append("AI ANALYSIS:\n\n");

        summary.append("Lead : ")
                .append(lead.getCustomerName())
                .append("\n");


        if (lead.getCompany() != null
                && !lead.getCompany().isBlank()) {

            summary.append("Company : ")
                    .append(lead.getCompany())
                    .append("\n");

        }


        summary.append("Source : ")
                .append(lead.getSource())
                .append("\n");


        summary.append("\nInsights:\n");


        if (lead.getEmail() != null) {

            summary.append(
                    "✓ Email Available\n"
            );

        }

        if (lead.getPhone() != null) {

            summary.append(
                    "✓ Phone Available\n"
            );

        }

        if (lead.getCompany() != null) {

            summary.append(
                    "✓ Company Information Available\n"
            );

        }


        summary.append(
                "\nRecommended Action:\n"
        );

        summary.append(
                "Contact within 24 hours."
        );


        return summary.toString();

    }



    // AI EMAIL GENERATOR

    public String generateAIEmail(Lead lead) {

        String subject;

        if (lead.getCompany() != null
                && !lead.getCompany().isBlank()) {

            subject =
                    "How Xeno CRM can help "
                            + lead.getCompany();

        }

        else {

            subject =
                    "Boost Your Business with Xeno CRM";

        }


        StringBuilder email =
                new StringBuilder();


        email.append("Subject: ")
                .append(subject)
                .append("\n\n");


        email.append("Hi ")
                .append(lead.getCustomerName())
                .append(",\n\n");


        email.append(
                "Thank you for showing interest in Xeno CRM.\n\n"
        );


        if (lead.getCompany() != null
                && !lead.getCompany().isBlank()) {

            email.append("We believe ")

                    .append(lead.getCompany())

                    .append(
                            " can improve customer management, sales tracking and follow-ups using our CRM solution.\n\n"
                    );

        }


        if (lead.getNotes() != null
                && !lead.getNotes().isBlank()) {

            email.append(
                            "We noticed your requirement: "
                    )

                    .append(lead.getNotes())

                    .append(".\n\n");

        }


        email.append(
                "Our team would love to schedule a quick demo and discuss how Xeno CRM can help your business grow.\n\n"
        );


        email.append("Regards,\n");

        email.append("Xeno CRM Team");


        return email.toString();

    }

}