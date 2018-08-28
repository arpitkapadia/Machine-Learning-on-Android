package com.example.android.mcproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.mindpipe.android.logging.log4j.LogConfigurator;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.pmml.jaxbbindings.False;
import weka.filters.Filter;
import weka.filters.supervised.instance.StratifiedRemoveFolds;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.supervised.instance.StratifiedRemoveFolds;


/**
 * Created by Pramodh on 11/29/2017.
 */

public class Nbc extends Fragment {
    //    variables
    private View fragmentView;
    private Spinner percentageSpinner;
    private Button btnGenerate;
    private int percentageSpinnerDefault = 42;
    Logger logger = Logger.getLogger("MCPROJECT");
    private String[] percentageValues = new String[maxPercentage];
    String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/iris.csv";

    //    constants
    private static final double trainPercentage = 70.0;
    private static final int minPercentage = 1;
    private static final int maxPercentage = 99;
    static boolean status_change = false;

    String outPath2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/data/file.data";

    public static Instances training_ins;
    public static Instances testing_ins;
    public static long beforeTrainingTime;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        fragmentView = inflater.inflate(R.layout.tab_nbc, container, false);
        percentageSpinner   = (Spinner) fragmentView.findViewById(R.id.percentage_spinner);
        //      init percentage spinner values
        initPercentageSpinner();
        //      set a default val to spinner
        percentageSpinner.setSelection(percentageSpinnerDefault);

        initControls();
        initLogger();
        return fragmentView;
    }

    private void initControls(){
        btnGenerate = (Button) fragmentView.findViewById(R.id.btnGenerateData);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Double percentage = trainPercentage;
                try{
                    percentage = Double.parseDouble(percentageSpinner.getSelectedItem().toString());
                }
                catch(NumberFormatException e) {
                    e.printStackTrace();
                }

                try {
                    Log.i("path",outPath);
                    ///storage/sdcard/Android/data/data.csv
                    ConverterUtils.DataSource source = new ConverterUtils.DataSource(outPath);
                    Instances data = source.getDataSet();

                    int trainSize = (int) Math.round(data.numInstances() * (percentage/100));
                    int testSize = data.numInstances() - trainSize;
                    Instances train = new Instances(data, 0, trainSize);
                    Instances test = new Instances(data, trainSize, testSize);

                    training_ins = train;
                    testing_ins = test;

                    System.out.println("********************************************************************************");
                    System.out.println(train);
                    System.out.println(" test********************************************************************************");
                    System.out.println(test);
                    System.out.println("********************************************************************************");

//                    NaiveBayes nb = new NaiveBayes();
                    train.setClassIndex(train.numAttributes()-1);
                    test.setClassIndex(train.numAttributes()-1);

                    ArrayList<String> ins = new ArrayList<String>();
                    int count = 0;
                    for(Instance i : train){
                        Log.i("instance ",i.toString());
                        ins.add(i.toString());
                        count++;
                    }
                    beforeTrainingTime = System.currentTimeMillis();
//                    nb.buildClassifier(train);

                    //set parameters to train model in aws
                    RequestClass inp_to_aws = new RequestClass("nb", ins);

                    Log.i("checking", "will it work for Instances");

                    Log.i("before","b");

                    File file = new File(outPath2);
                    boolean deleted = file.delete();


                    String nb1 = trainInCloud(inp_to_aws, test);

                    status_change = false;

                    readAWSFile();



                }
                catch(Exception ex) {
//                    Toast.makeText(v.getContext(), "Error: " + ex.getCause(), Toast.LENGTH_SHORT);
                    ex.printStackTrace();
                }

            }

        });
    }

    public void readAWSFile(){
        final CallAWS testingAWSCall1 = new CallAWS(getActivity().getApplicationContext());
        AmazonS3 s3 = new AmazonS3Client(testingAWSCall1.getCredentialsProvider());
        TransferUtility transferUtility = new TransferUtility(s3, getActivity().getApplicationContext());
        File objectFile = new File(outPath2);
        TransferObserver observer = transferUtility.download(
                "lambda-bucket-arpit", "trained_nb",

                objectFile        /* The file to download the object to */
        );


        observer.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                // do something
                Log.i("State", state.toString());
                status_change = true;
                if(state.toString() == "COMPLETED"){
                    runTesing();
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/(bytesTotal+1) * 100);
                //Display percentage transfered to user
                Log.i("progress", String.valueOf(percentage));
            }

            @Override
            public void onError(int id, Exception ex) {
                // do something
                Log.e("Error:", ex.getMessage());
            }

        });

        Log.i("path", String.valueOf(observer.getBytesTransferred()));
        Log.i("got","file");

    }

    public void runTesing(){
        // deserialize model
//                    File temp = new File(outPath2);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(outPath2));
            NaiveBayes nb = (NaiveBayes) ois.readObject();


            System.out.print(nb.toString());

            System.out.print(testing_ins);
            System.out.print(training_ins);



            long trainingTime = System.currentTimeMillis() - beforeTrainingTime;
            Evaluation eval = new Evaluation(testing_ins);
            long beforeTestingTime = System.currentTimeMillis();
            eval.crossValidateModel(nb, testing_ins, 2, new Random(1));
            long testingTime = System.currentTimeMillis() - beforeTestingTime;

            System.out.println("Precision is: "+ eval.precision(1));
            System.out.println("Recall is: "+ eval.recall(1));
            System.out.println("False Reject Rate is: "+ eval.falseNegativeRate(1));
            System.out.println("False Accept Rate is: "+ eval.falsePositiveRate(1));
            System.out.println("True Accept Rate is: "+ eval.truePositiveRate(1));
            System.out.println("True Reject Rate is: "+ eval.trueNegativeRate(1));
            System.out.println("HTER (FAR+FRR)/2) is : "+ (eval.falsePositiveRate(1)+eval.falseNegativeRate(1))/2);
            System.out.println("Time required for training in mili seconds: "+ trainingTime);
            System.out.println("Time required for testing in mili seconds: "+ testingTime);


            logger.info("Precision is: "+ eval.precision(1));
            logger.info("Recall is: "+ eval.recall(1));
            logger.info("False Reject Rate is: "+ eval.falseNegativeRate(1));
            logger.info("False Accept Rate is: "+ eval.falsePositiveRate(1));
            logger.info("True Accept Rate is: "+ eval.truePositiveRate(1));
            logger.info("True Reject Rate is: "+ eval.trueNegativeRate(1));
            logger.info("HTER (FAR+FRR)/2) is : "+ (eval.falsePositiveRate(1)+eval.falseNegativeRate(1))/2);
            logger.info("Time required for training in mili seconds: "+ trainingTime);
            logger.info("Time required for testing in mili seconds: "+ testingTime);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public String trainInCloud(RequestClass inp, Instances test) {

        Log.i("in cloud","b");

        final CallAWS testingAWSCall1 = new CallAWS(getActivity().getApplicationContext());
        LambdaInvokerFactory factory = testingAWSCall1.getLambdaInvokeFactory();
        final LambdaFunctionInterface myInterface1 = factory.build(LambdaFunctionInterface.class);

        class TrainModel extends AsyncTask<RequestClass, Void, ResponseClass>{
            public String trained;
            @Override
            protected ResponseClass doInBackground(RequestClass...params) {
                // invoke "echo" method. In case it fails, it will throw a
                // LambdaFunctionException.
                try {
                    Log.i("before call","b");


                    //function call
                    ResponseClass df = myInterface1.trainClassifier4(params[0]);

                    return df;


                } catch (LambdaFunctionException lfe) {
                    Log.e("Lmbda Message", "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ResponseClass result) {
                if (result == null) {
                    return;
                }
                if(result.getOutput()==null){

//                    System.out.println(result.getOutput());
                    Toast.makeText(getActivity().getApplicationContext(), "null returned", Toast.LENGTH_LONG).show();

                }
                trained = result.getOutput();
//                System.out.println(trained);


//                Toast.makeText(MainActivity.this, result.getOutput().toString(), Toast.LENGTH_LONG).show();

                // Do a toast
                Toast.makeText(getActivity().getApplicationContext(), "Model Trained Successfully", Toast.LENGTH_LONG).show();
            }

            public String getTrainedClassifier(){
                System.out.println(trained);
                return trained;
            }

        }
        TrainModel newModel = new TrainModel();
//        RequestClass[] rc = new RequestClass[]{inp};
        try {
            newModel.execute(inp).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return newModel.getTrainedClassifier();
    }

    private void initPercentageSpinner(){
//        ref: https://androidforums.com/threads/spinner-with-numbers.132526/
        for (int i = maxPercentage; i > 0; i--){
            percentageValues[maxPercentage - i] = Integer.toString(i);
        }

        ArrayAdapter<String> percentageAA = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, percentageValues);
        percentageSpinner.setAdapter(percentageAA);
    }


    private void initLogger() {
        Context ctx = getActivity().getApplicationContext();
        LogConfigurator configurator = new LogConfigurator();
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data" + File.separator + ctx.getPackageName());

        if (!file.exists()) {
            file.mkdirs();
        }

        String logFileName = file + File.separator + "logs.txt";
        configurator.setFileName(logFileName);
        configurator.setRootLevel(Level.INFO);
        configurator.setFilePattern("[%-5p] %d - %m%n");
        configurator.configure();
    }
}
