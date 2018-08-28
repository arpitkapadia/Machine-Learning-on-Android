package com.amazonaws.lambda.demo;


import java.util.ArrayList;

import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 * Created by ak on 11/25/17.
 */
//public class RequestClass {
//
//    private String inputClassifier;
//    private ConverterUtils.DataSource trainingInstances;
//
//    public RequestClass(String inp, ConverterUtils.DataSource training_inp){
//        setInputClassifier(inp);
//        setTrainingInstances(training_inp);
//    }
//
//    public void setInputClassifier(String inp1){
//        this.inputClassifier = inp1;
//    }
//
//    public RequestClass() {
//    }
//
//    public void setTrainingInstances(ConverterUtils.DataSource train_inp){
//        this.trainingInstances = train_inp;
//    }
//
//    public String getInputClassifier(){
//        return this.inputClassifier;
//    }
//
//    public ConverterUtils.DataSource getTrainingInstances(){
//        return this.trainingInstances;
//    }
//
//}
public class RequestClass {

    private String inputClassifier;
    private ArrayList<String> trainingInstances;

    public RequestClass(String inp, ArrayList<String> training_inp){
        setInputClassifier(inp);
        setTrainingInstances(training_inp);
    }

    public void setInputClassifier(String inp1){
        this.inputClassifier = inp1;
    }

    public RequestClass() {
    }

    public void setTrainingInstances(ArrayList<String> train_inp){
        this.trainingInstances = train_inp;
    }

    public String getInputClassifier(){
        return this.inputClassifier;
    }

    public ArrayList<String> getTrainingInstances(){
        return this.trainingInstances;
    }

}
//public class RequestClass {
//
//    private String inputClassifier;
//    private String trainingInstances;
//
//    public RequestClass(String inp, String training_inp){
//        setInputClassifier(inp);
//        setTrainingInstances(training_inp);
//    }
//
//    public void setInputClassifier(String inp1){
//        this.inputClassifier = inp1;
//    }
//
//    public RequestClass() {
//    }
//
//    public void setTrainingInstances(String train_inp){
//        this.trainingInstances = train_inp;
//    }
//
//    public String getInputClassifier(){
//        return inputClassifier;
//    }
//
//    public String getTrainingInstances(){
//        return trainingInstances;
//    }
//}
