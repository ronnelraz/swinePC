package com.ronnelrazo.physical_counting.model;

public class model_breeder {

    public String location_Code,location_Name,org_Code,org_Name,farm_Org,farm_Name,female_Stock,Male_Stock,stock_Balance;

    public model_breeder(String location_Code, String location_Name, String org_Code, String org_Name, String farm_Org, String farm_Name, String female_Stock, String male_Stock, String stock_Balance) {
        this.location_Code = location_Code;
        this.location_Name = location_Name;
        this.org_Code = org_Code;
        this.org_Name = org_Name;
        this.farm_Org = farm_Org;
        this.farm_Name = farm_Name;
        this.female_Stock = female_Stock;
        this.Male_Stock = male_Stock;
        this.stock_Balance = stock_Balance;
    }

    public String getLocation_Code() {
        return location_Code;
    }

    public void setLocation_Code(String location_Code) {
        this.location_Code = location_Code;
    }

    public String getLocation_Name() {
        return location_Name;
    }

    public void setLocation_Name(String location_Name) {
        this.location_Name = location_Name;
    }

    public String getOrg_Code() {
        return org_Code;
    }

    public void setOrg_Code(String org_Code) {
        this.org_Code = org_Code;
    }

    public String getOrg_Name() {
        return org_Name;
    }

    public void setOrg_Name(String org_Name) {
        this.org_Name = org_Name;
    }

    public String getFarm_Org() {
        return farm_Org;
    }

    public void setFarm_Org(String farm_Org) {
        this.farm_Org = farm_Org;
    }

    public String getFarm_Name() {
        return farm_Name;
    }

    public void setFarm_Name(String farm_Name) {
        this.farm_Name = farm_Name;
    }

    public String getFemale_Stock() {
        return female_Stock;
    }

    public void setFemale_Stock(String female_Stock) {
        this.female_Stock = female_Stock;
    }

    public String getMale_Stock() {
        return Male_Stock;
    }

    public void setMale_Stock(String male_Stock) {
        Male_Stock = male_Stock;
    }

    public String getStock_Balance() {
        return stock_Balance;
    }

    public void setStock_Balance(String stock_Balance) {
        this.stock_Balance = stock_Balance;
    }
}
