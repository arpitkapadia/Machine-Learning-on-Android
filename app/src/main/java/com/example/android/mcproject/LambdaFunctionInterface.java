package com.example.android.mcproject;

/**
 * Created by ak on 11/25/17.
 */
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import weka.classifiers.Classifier;

/*
 * A holder for lambda functions
 */
public interface LambdaFunctionInterface {

    /**
     * Invoke lambda function "echo". The function name is the method name
     */
    @LambdaFunction
    String ml(String nameInfo);

    @LambdaFunction(invocationType="RequestResponse", functionName="trainClassifier")
    ResponseClass trainClassifier(RequestClass inp);

//    Object trainClassifier2(Object inp);

    @LambdaFunction(invocationType="RequestResponse", functionName="trainClassifier4")
    ResponseClass trainClassifier4(RequestClass req);


    @LambdaFunction(invocationType="RequestResponse", functionName="trainKNN")
    ResponseClass trainKNN(RequestClass req);






//    /**
//     * Invoke lambda function "echo". The functionName in the annotation
//     * overrides the default which is the method name
//     */
//    @LambdaFunction(functionName = "echo")
//    void noEcho(String nameInfo);
}