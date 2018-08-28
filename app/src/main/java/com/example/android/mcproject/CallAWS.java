package com.example.android.mcproject;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;

/**
 * Created by ak on 11/25/17.
 */

public class CallAWS {

    private CognitoCachingCredentialsProvider credentialsProvider;

    private  LambdaInvokerFactory factory;

    public CallAWS(Context context){
        setCredentialProvider(context);
        setLambdaInvokeFactory(context);
    }

    private void setCredentialProvider(Context context){
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:145a496d-944b-453e-a449-6f5de462c0d6",
                Regions.US_EAST_1
        );
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    private void setLambdaInvokeFactory(Context context) {

        // Create a LambdaInvokerFactory, to be used to instantiate the Lambda proxy
        factory = new LambdaInvokerFactory(
                context,
                Regions.US_EAST_1,
                this.credentialsProvider);
    }

    public LambdaInvokerFactory getLambdaInvokeFactory() {
        return factory;
    }

}
