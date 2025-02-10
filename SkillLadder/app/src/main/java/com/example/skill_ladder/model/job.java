package com.example.skill_ladder.model;

public class job {
    String companyName;
    String companyEmail;
    String companyMobile;
    String jobTitle;
    String jobDate;

    public job() {
    }

    public job(String companyName, String companyEmail, String companyMobile, String jobTitle, String jobDate) {
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.companyMobile = companyMobile;
        this.jobTitle = jobTitle;
        this.jobDate = jobDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String companyMobile) {
        this.companyMobile = companyMobile;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }
}
