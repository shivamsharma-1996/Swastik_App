package com.swastikenterprises.Home.Dashboard.Home;

import java.util.Map;

public class Opportunity
{
 
    private String email;
    private String profile;
    private String cv_name;
    private String cv_url;
    private Map timestamp;


    public Opportunity() {
    }

    public Opportunity(String email, String profile, String cv_name, String cv_url, Map timestamp)
    {
        this.email = email;
        this.profile = profile;
        this.cv_name = cv_name;
        this.cv_url = cv_url;
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCv_name() {
        return cv_name;
    }

    public void setCv_name(String cv_name) {
        this.cv_name = cv_name;
    }

    public String getCv_url() {
        return cv_url;
    }

    public void setCv_url(String cv_url) {
        this.cv_url = cv_url;
    }

    public Map getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map timestamp) {
        this.timestamp = timestamp;
    }
}