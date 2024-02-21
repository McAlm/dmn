package com.camunda.demo;


public class MyRule {
    private String ageInput;
    private String colorInput;
    private String riskOutput;

    
    public MyRule(String ageInput, String colorInput, String riskOutput) {
        this.ageInput = ageInput;
        this.colorInput = colorInput;
        this.riskOutput = riskOutput;
    }
    public String getAgeInput() {
        return ageInput;
    }
    public void setAgeInput(String ageInput) {
        this.ageInput = ageInput;
    }
    public String getColorInput() {
        return colorInput;
    }
    public void setColorInput(String colorInput) {
        this.colorInput = colorInput;
    }
    public String getRiskOutput() {
        return riskOutput;
    }
    public void setRiskOutput(String riskOutput) {
        this.riskOutput = riskOutput;
    }

}
