package com.ronnelrazo.physical_counting.model;

public class model_med {

    public String orgCode,period,projectCode,farmCode,farmOrg,FarnName,productCode,productName,stockUnit,stockQty,stockWgh;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFarmCode() {
        return farmCode;
    }

    public void setFarmCode(String farmCode) {
        this.farmCode = farmCode;
    }

    public String getFarmOrg() {
        return farmOrg;
    }

    public void setFarmOrg(String farmOrg) {
        this.farmOrg = farmOrg;
    }

    public String getFarnName() {
        return FarnName;
    }

    public void setFarnName(String farnName) {
        FarnName = farnName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStockUnit() {
        return stockUnit;
    }

    public void setStockUnit(String stockUnit) {
        this.stockUnit = stockUnit;
    }

    public String getStockQty() {
        return stockQty;
    }

    public void setStockQty(String stockQty) {
        this.stockQty = stockQty;
    }

    public String getStockWgh() {
        return stockWgh;
    }

    public void setStockWgh(String stockWgh) {
        this.stockWgh = stockWgh;
    }

    public model_med(String orgCode, String period, String projectCode, String farmCode, String farmOrg, String farnName, String productCode, String productName, String stockUnit, String stockQty, String stockWgh) {
        this.orgCode = orgCode;
        this.period = period;
        this.projectCode = projectCode;
        this.farmCode = farmCode;
        this.farmOrg = farmOrg;
        FarnName = farnName;
        this.productCode = productCode;
        this.productName = productName;
        this.stockUnit = stockUnit;
        this.stockQty = stockQty;
        this.stockWgh = stockWgh;
    }
}
