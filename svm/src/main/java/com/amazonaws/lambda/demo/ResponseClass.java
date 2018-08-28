package com.amazonaws.lambda.demo;

public class ResponseClass{
    String output;
    public ResponseClass(String cls) {
        output = cls;
    }
    public ResponseClass() {
    }
    public void setOutput(String cls) {
        output = cls;
    }
    public String getOutput() {
        return output;
    }
}
