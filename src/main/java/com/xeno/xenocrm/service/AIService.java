package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.Lead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xeno.xenocrm.repository.CustomerRepository;
import com.xeno.xenocrm.repository.LeadRepository;
@Service
public class AIService {

    @Autowired
    private GeminiService geminiService;

    // ===========================
    // AI Lead Scoring
    // ===========================
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LeadRepository leadRepository;
    public int calculateLeadScore(Lead lead) {

        int score = 0;

        // Referral / Source Score
        if (lead.getSource() != null) {

            String source = lead.getSource().toLowerCase();

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

        // Company Available
        if (lead.getCompany() != null &&
                !lead.getCompany().isBlank()) {

            score += 20;
        }

        // Notes Quality
        if (lead.getNotes() != null &&
                lead.getNotes().length() > 50) {

            score += 20;

        } else if (lead.getNotes() != null &&
                lead.getNotes().length() > 20) {

            score += 10;
        }

        // Email Score
        if (lead.getEmail() != null &&
                !lead.getEmail().isBlank()) {

            String email = lead.getEmail().toLowerCase();

            if (email.endsWith("@gmail.com")) {

                score += 10;

            } else {

                score += 25;
            }
        }

        // Phone Available
        if (lead.getPhone() != null &&
                !lead.getPhone().isBlank()) {

            score += 15;
        }

        return Math.min(score, 100);

    }

    // ===========================
    // Priority
    // ===========================

    public String getPriority(int score) {

        if (score >= 80) {

            return "HOT LEAD 🔥";

        } else if (score >= 50) {

            return "WARM LEAD ⭐";

        }

        return "COLD LEAD ❄";
    }
    // ===========================
    // AI Lead Summary
    // ===========================

    public String generateSummary(Lead lead) {

        StringBuilder summary = new StringBuilder();

        summary.append("🤖 AI Lead Analysis\n\n");

        summary.append("Lead Name : ")
                .append(lead.getCustomerName())
                .append("\n");

        if (lead.getCompany() != null &&
                !lead.getCompany().isBlank()) {

            summary.append("Company : ")
                    .append(lead.getCompany())
                    .append("\n");
        }

        if (lead.getSource() != null) {

            summary.append("Source : ")
                    .append(lead.getSource())
                    .append("\n");
        }

        summary.append("\nInsights\n");

        if (lead.getEmail() != null &&
                !lead.getEmail().isBlank()) {

            if (lead.getEmail().toLowerCase().endsWith("@gmail.com")) {

                summary.append("• Personal Email Detected\n");

            } else {

                summary.append("• Corporate Email Detected\n");
            }
        }

        if (lead.getCompany() != null &&
                !lead.getCompany().isBlank()) {

            summary.append("• Company Information Available\n");
        }

        if (lead.getSource() != null &&
                lead.getSource().equalsIgnoreCase("Referral")) {

            summary.append("• Referral Lead (High Conversion Probability)\n");
        }

        summary.append("\nRecommendation\n");
        summary.append("Contact within 24 hours for maximum conversion.");

        return summary.toString();
    }


    // ===========================
    // AI Email Generator
    // ===========================

    public String generateAIEmail(Lead lead) {

        String subject;

        if (lead.getCompany() != null &&
                !lead.getCompany().isBlank()) {

            subject = "How Xeno CRM can help " + lead.getCompany();

        } else {

            subject = "Boost Your Business with Xeno CRM";
        }

        StringBuilder email = new StringBuilder();

        email.append("Subject: ")
                .append(subject)
                .append("\n\n");

        email.append("Hi ")
                .append(lead.getCustomerName())
                .append(",\n\n");

        email.append("Thank you for showing interest in Xeno CRM.\n\n");

        if (lead.getCompany() != null &&
                !lead.getCompany().isBlank()) {

            email.append("We believe ")
                    .append(lead.getCompany())
                    .append(" can improve customer management, sales tracking, automation and customer engagement using Xeno CRM.\n\n");
        }

        if (lead.getNotes() != null &&
                !lead.getNotes().isBlank()) {

            email.append("We noticed your requirement:\n")
                    .append(lead.getNotes())
                    .append("\n\n");
        }

        email.append("We would love to schedule a quick demo and show how Xeno CRM can help your business grow.\n\n");

        email.append("Regards,\n");
        email.append("Xeno CRM Team");

        return email.toString();
    }
    // ===========================
    // Gemini AI Campaign Generator
    // ===========================

    public String generateCampaign(String prompt) {

        String fullPrompt = """
You are an Enterprise CRM AI Assistant.

Generate a professional CRM marketing campaign.

User Request:
%s

Return a clean, professional response with the following headings:

🎯 Audience
📧 Recommended Channel
📝 Subject
🎁 Offer
⏰ Best Time
📈 Expected Open Rate
📊 Expected CTR
🤖 AI Recommendation

Do not return JSON.
Do not return markdown.
Return only readable text.
""".formatted(prompt);

        return geminiService.generateContent(fullPrompt);
    }
    // ===========================
// AI Chat Assistant
// ===========================

    public String chatWithAI(String question) {

        int customers = customerRepository.findAll().size();

        int leads = leadRepository.findAll().size();

        long vipCustomers =
                customerRepository.findAll()
                        .stream()
                        .filter(c ->
                                c.getSegment() != null &&
                                        c.getSegment().contains("VIP"))
                        .count();

        long inactiveCustomers =
                customerRepository.findAll()
                        .stream()
                        .filter(c ->
                                c.getStatus() != null &&
                                        c.getStatus().equalsIgnoreCase("INACTIVE"))
                        .count();

        long hotLeads =
                leadRepository.findAll()
                        .stream()
                        .filter(l ->
                                l.getAiScore() != null &&
                                        l.getAiScore() >= 80)
                        .count();

        String prompt = """
You are Xeno CRM AI Assistant.

You are connected to a real CRM system.

CRM Statistics

Total Customers : %d

Total Leads : %d

VIP Customers : %d

Inactive Customers : %d

Hot Leads : %d

User Question:

%s

Answer professionally.

Use the CRM statistics while answering.

Keep answer under 250 words.

If appropriate include:

- Recommendation
- Next Action
- Priority
- Reason
""".formatted(
                customers,
                leads,
                vipCustomers,
                inactiveCustomers,
                hotLeads,
                question
        );

        return geminiService.generateContent(prompt);

    }

}