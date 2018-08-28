package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

import weka.classifiers.Classifier;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class TrainClassifierTest {

    private static RequestClass input;

    @BeforeClass
    public static void createInput() throws IOException {
        // TODO: set up your sample input object here.
        ArrayList<String> ins = new ArrayList<String>();
        ins.add("4.9,3,1.4,0.2,Iris-setosa");
        ins.add("4.7,3.2,1.3,0.2,Iris-setosa");

        input = new RequestClass("nb", ins);
        
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("trainModel");

        return ctx;
    }

    @Test
    public void testTrainClassifier() {
        TrainClassifier handler = new TrainClassifier();
        Context ctx = createContext();

        ResponseClass output = handler.handleRequest(input, ctx);
//        System.out.println("bc wtf" + output.getOutput().toString());
//        output.getOutput().classifyInstance(arg0)

        System.out.println(output.getOutput().getClass());

        // TODO: validate output here if needed.
//        Assert.assertEquals("Hello from Lambda!", output);
    }
}
