package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.Lead;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    public int calculateLeadScore(Lead lead) {

        int score = 0;

        if (lead.getSource() != null) {

            String source =

                    lead.getSource()

                            .toLowerCase();


            if (source.contains("linkedin")) {

                score += 30;

            }

            else if (

                    source.contains("website")

            ) {

                score += 20;

            }

            else {

                score += 10;

            }

        }



        if (

                lead.getCompany() != null

                        &&

                        !lead.getCompany().isBlank()

        ) {

            score += 20;

        }



        if (

                lead.getNotes() != null

                        &&

                        lead.getNotes().length() > 20

        ) {

            score += 20;

        }



        if (

                lead.getEmail() != null

                        &&

                        !lead.getEmail().isBlank()

        ) {

            score += 15;

        }



        if (

                lead.getPhone() != null

                        &&

                        !lead.getPhone().isBlank()

        ) {

            score += 15;

        }


        return Math.min(score, 100);

    }






    public String getPriority(int score) {

        if (score >= 85) {

            return "HOT 🔥";

        }

        else if (score >= 60) {

            return "MEDIUM";

        }

        else {

            return "LOW";

        }

    }







    public String generateSummary(Lead lead) {

        StringBuilder summary =

                new StringBuilder();


        summary.append(

                        "Customer "

                )

                .append(

                        lead.getCustomerName()

                )

                .append(

                        " is interested in "

                );



        if (

                lead.getCompany() != null

        ) {

            summary.append(

                    lead.getCompany()

            );

        }



        summary.append(". ");



        if (

                lead.getNotes() != null

        ) {

            summary.append(

                            "Notes: "

                    )

                    .append(

                            lead.getNotes()

                    )

                    .append(". ");

        }



        summary.append(

                "Recommended Follow Up within 48 hours."

        );


        return summary.toString();

    }








    // ===== AI EMAIL GENERATOR =====

    public String generateEmail(Lead lead) {

        StringBuilder email =

                new StringBuilder();



        email.append(

                "Subject: Enterprise CRM Demo\n\n"

        );



        email.append(

                        "Hi "

                )

                .append(

                        lead.getCustomerName()

                )

                .append(

                        ",\n\n"

                );



        email.append(

                "Thank you for showing interest in our Enterprise CRM.\n\n"

        );



        if (

                lead.getCompany() != null

                        &&

                        !lead.getCompany().isBlank()

        ) {

            email.append(

                            "We believe our CRM can help "

                    )

                    .append(

                            lead.getCompany()

                    )

                    .append(

                            " improve customer management and business growth.\n\n"

                    );

        }



        email.append(

                "We would love to schedule a demo and discuss your requirements.\n\n"

        );



        email.append(

                "Regards,\n"

        );



        email.append(

                "Xeno CRM Team"

        );

        return email.toString();

    }

}