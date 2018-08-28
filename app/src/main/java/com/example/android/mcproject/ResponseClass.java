package com.example.android.mcproject;

/**
 * Created by ak on 11/25/17.
 */

import java.io.Serializable;

import weka.classifiers.Classifier;

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