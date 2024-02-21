package com.camunda.demo;

import java.io.File;

import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.InputEntry;
import org.camunda.bpm.model.dmn.instance.InputExpression;
import org.camunda.bpm.model.dmn.instance.Output;
import org.camunda.bpm.model.dmn.instance.OutputEntry;
import org.camunda.bpm.model.dmn.instance.Rule;
import org.camunda.bpm.model.dmn.instance.Text;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        DmnModelInstance modelInstance = Dmn.createEmptyModel();
        Definitions definitions = modelInstance.newInstance(Definitions.class);
        definitions.setNamespace("http://camunda.org/schema/1.0/dmn");
        definitions.setName("definitions");
        definitions.setId("definitions");
        definitions.setAttributeValueNs("http://camunda.org/schema/modeler/1.0",
                "modeler:executionPlatformVersion", "8.3.0");
        definitions.setAttributeValueNs("http://camunda.org/schema/modeler/1.0",
                "modeler:executionPlatform", "Camunda Cloud");

        modelInstance.setDefinitions(definitions);

        Decision decision = modelInstance.newInstance(Decision.class);
        decision.setId("testGenerated");
        decision.setName("generationtest");
        definitions.addChildElement(decision);

        // add decision table
        DecisionTable table = modelInstance.newInstance(DecisionTable.class);
        decision.addChildElement(table);

        table.setId("myDecisionTable");
        table.setHitPolicy(HitPolicy.UNIQUE);

        // first input
        Input ageInput = modelInstance.newInstance(Input.class);
        table.addChildElement(ageInput);
        ageInput.setLabel("Age");
        ageInput.setId("InputClause_age");
        InputExpression ageExpression = modelInstance.newInstance(InputExpression.class);
        ageInput.addChildElement(ageExpression);
        ageExpression.setId("ageExpression");
        ageExpression.setTypeRef("integer");
        ageExpression.setExpressionLanguage("feel");
        Text ageText = modelInstance.newInstance(Text.class);
        ageText.setTextContent("age");
        ageExpression.setText(ageText);

        // second input
        Input colorInput = modelInstance.newInstance(Input.class);
        table.addChildElement(colorInput);
        colorInput.setLabel("Color");
        colorInput.setId("InputClause_color");
        InputExpression colorExpression = modelInstance.newInstance(InputExpression.class);
        colorInput.addChildElement(colorExpression);
        colorExpression.setId("colorExpression");
        colorExpression.setTypeRef("string");
        colorExpression.setExpressionLanguage("feel");
        Text colorText = modelInstance.newInstance(Text.class);
        colorText.setTextContent("color");
        colorExpression.setText(colorText);

        // output
        Output riskOutput = modelInstance.newInstance(Output.class);
        table.addChildElement(riskOutput);
        riskOutput.setId("OutoutClause_risk");
        riskOutput.setName("risk");
        riskOutput.setLabel("Risk");
        riskOutput.setTypeRef("string");

        // now let's add some rules, e.g. for each row from a CSV file:
        MyRule rule1 = new MyRule("[0..17]", "-", "\"low\"");
        addRule(modelInstance, table, rule1, 1);
        
        MyRule rule2 = new MyRule("[18..100]", "\"red\"", "\"high\"");
        addRule(modelInstance, table, rule2, 2);

        MyRule rule3 = new MyRule("[18..100]", "-", "\"medium\"");
        addRule(modelInstance, table, rule3, 3);


        // validate the model
        Dmn.validateModel(modelInstance);

        // convert to string
        String xmlString = Dmn.convertToString(modelInstance);

        // write to file
        File file = new File("MyDmn.dmn");
        Dmn.writeModelToFile(file, modelInstance);
    }

    private static void addRule(DmnModelInstance modelInstance, DecisionTable table, MyRule myRule,
            Integer ruleCounter) {
        Rule rule = modelInstance.newInstance(Rule.class);
        table.addChildElement(rule);
        rule.setId("DecisionRule_" + ruleCounter);

        // the order of inputEntries depends on the order of Inputs in the Decision
        // Table
        InputEntry inputEntry = modelInstance.newInstance(InputEntry.class);
        rule.addChildElement(inputEntry);
        inputEntry.setId("UnaryTestsAge_" + ruleCounter);
        Text inputEntryText = modelInstance.newInstance(Text.class);
        inputEntry.addChildElement(inputEntryText);
        inputEntryText.setTextContent(myRule.getAgeInput());

        inputEntry = modelInstance.newInstance(InputEntry.class);
        rule.addChildElement(inputEntry);
        inputEntry.setId("UnaryTestsColor_" + ruleCounter);
        inputEntryText = modelInstance.newInstance(Text.class);
        inputEntry.addChildElement(inputEntryText);
        inputEntryText.setTextContent(myRule.getColorInput());

        OutputEntry riskOutputEntry = modelInstance.newInstance(OutputEntry.class);
        rule.addChildElement(riskOutputEntry);
        riskOutputEntry.setId("OutputEntryRisk_" + ruleCounter);
        Text riskOutputText = modelInstance.newInstance(Text.class);
        riskOutputEntry.addChildElement(riskOutputText);
        riskOutputText.setTextContent(myRule.getRiskOutput());
    }
}
