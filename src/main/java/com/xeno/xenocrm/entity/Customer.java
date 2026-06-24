package com.xeno.xenocrm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String company;

    private String status;


    // ===== AI FIELDS =====

    private String segment;

    private Integer churnRisk;

    @Column(length = 1000)
    private String aiInsight;

    private String revenuePotential;


    public Customer() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }



    public Integer getChurnRisk() {
        return churnRisk;
    }

    public void setChurnRisk(Integer churnRisk) {
        this.churnRisk = churnRisk;
    }



    public String getAiInsight() {
        return aiInsight;
    }

    public void setAiInsight(String aiInsight) {
        this.aiInsight = aiInsight;
    }



    public String getRevenuePotential() {
        return revenuePotential;
    }

    public void setRevenuePotential(String revenuePotential) {
        this.revenuePotential = revenuePotential;
    }

}