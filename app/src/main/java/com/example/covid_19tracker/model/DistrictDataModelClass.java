package com.example.covid_19tracker.model;

public class DistrictDataModelClass {

    String district, active, deceased;

    public DistrictDataModelClass(String district, String active, String deceased) {
        this.district = district;
        this.active = active;
        this.deceased = deceased;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public DistrictDataModelClass() {
    }
}
