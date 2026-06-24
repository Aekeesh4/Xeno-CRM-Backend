package com.xeno.xenocrm.service;

import com.xeno.xenocrm.entity.Customer;
import com.xeno.xenocrm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ActivityService activityService;


    public Customer saveCustomer(Customer customer) {


        // ===== AI CUSTOMER SEGMENT =====

        String segment;

        int churnRisk;

        String aiInsight;

        String revenuePotential;



        if (customer.getCompany() != null
                && !customer.getCompany().isBlank()
                && customer.getEmail() != null
                && !customer.getEmail().endsWith("@gmail.com")) {

            segment = "VIP CUSTOMER ⭐";

            churnRisk = 10;

            revenuePotential = "HIGH";

            aiInsight =
                    "High value customer with strong business profile.";


        }

        else if (customer.getStatus() != null
                && customer.getStatus().equalsIgnoreCase("INACTIVE")) {

            segment = "AT RISK ⚠";

            churnRisk = 80;

            revenuePotential = "LOW";

            aiInsight =
                    "Customer inactive. Retention campaign recommended.";

        }

        else {

            segment = "REGULAR CUSTOMER";

            churnRisk = 35;

            revenuePotential = "MEDIUM";

            aiInsight =
                    "Regular customer with moderate engagement.";

        }



        customer.setSegment(segment);

        customer.setChurnRisk(churnRisk);

        customer.setAiInsight(aiInsight);

        customer.setRevenuePotential(revenuePotential);



        Customer savedCustomer =
                customerRepository.save(customer);



        activityService.saveActivity(

                "Customer "

                        + savedCustomer.getName()

                        + " added | Segment : "

                        + savedCustomer.getSegment()

        );


        return savedCustomer;

    }



    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();

    }



    public void deleteCustomer(Long id) {

        Customer customer =

                customerRepository.findById(id)

                        .orElse(null);


        if (customer != null) {

            activityService.saveActivity(

                    "Customer "

                            + customer.getName()

                            + " deleted"

            );


            customerRepository.deleteById(id);

        }

    }




    public Customer updateCustomer(

            Long id,

            Customer customerDetails) {


        Customer customer =

                customerRepository.findById(id)

                        .orElseThrow(() ->

                                new RuntimeException(

                                        "Customer not found"

                                )

                        );


        customer.setName(

                customerDetails.getName()

        );


        customer.setEmail(

                customerDetails.getEmail()

        );


        customer.setPhone(

                customerDetails.getPhone()

        );


        customer.setCompany(

                customerDetails.getCompany()

        );


        customer.setStatus(

                customerDetails.getStatus()

        );



        // Recalculate AI

        if (customer.getCompany() != null
                && !customer.getCompany().isBlank()
                && customer.getEmail() != null
                && !customer.getEmail().endsWith("@gmail.com")) {

            customer.setSegment("VIP CUSTOMER ⭐");

            customer.setChurnRisk(10);

            customer.setRevenuePotential("HIGH");

            customer.setAiInsight(
                    "High value customer with strong business profile."
            );

        }

        else if (customer.getStatus() != null
                && customer.getStatus().equalsIgnoreCase("INACTIVE")) {

            customer.setSegment("AT RISK ⚠");

            customer.setChurnRisk(80);

            customer.setRevenuePotential("LOW");

            customer.setAiInsight(
                    "Customer inactive. Retention campaign recommended."
            );

        }

        else {

            customer.setSegment("REGULAR CUSTOMER");

            customer.setChurnRisk(35);

            customer.setRevenuePotential("MEDIUM");

            customer.setAiInsight(
                    "Regular customer with moderate engagement."
            );

        }



        Customer updatedCustomer =

                customerRepository.save(customer);



        activityService.saveActivity(

                "Customer "

                        + updatedCustomer.getName()

                        + " updated"

        );


        return updatedCustomer;

    }



    public Customer getCustomerByEmail(

            String email) {

        return customerRepository.findByEmail(email);

    }

}