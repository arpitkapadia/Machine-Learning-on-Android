package com.amazonaws.lambda.demo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.converters.ConverterUtils;
import weka.core.NominalAttributeInfo;
public class TrainClassifier implements RequestHandler<RequestClass, ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass input, Context context) {
        context.getLogger().log("Input: " + input);
     // Create list to hold nominal values "first", "second", "third" 
        
        List<String> my_nominal_values = new ArrayList<String>(3); 
        my_nominal_values.add("Iris-versicolor"); 
        my_nominal_values.add("Iris-setosa"); 
        my_nominal_values.add("Iris-virginica"); 

        // Create nominal attribute "position" 
        Attribute position = new Attribute("att5", my_nominal_values);
        
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(new Attribute("att1"));
        attributes.add(new Attribute("att2"));
        attributes.add(new Attribute("att3"));
        attributes.add(new Attribute("att4"));
        attributes.add(position);
//        attributes.add(new Attribute("att5",true));
        

        
        Instances train_instances = new Instances("training",attributes, 500 );
        for(String ins: input.getTrainingInstances()) {
        		String[] arr = ins.split(",");
        		System.out.println(ins);
        		
        		Instance inst = new DenseInstance(5); 

        		// Set instance's values for the attributes "length", "weight", and "position"
        		for(int i=0; i<4; i++) {
            		inst.setValue(attributes.get(i), Float.parseFloat(arr[i])); 

        		}
        		System.out.println(arr[4]);
        		inst.setValue(attributes.get(4), arr[4]);

        		train_instances.add(inst);

        		// Print the instance 
//        		System.out.println("The instance: " + inst); 
//        		System.out.println("The instances: " + train_instances); 

        }
//        train_instances.setClass(attributes.get(4));
        train_instances.setClassIndex(train_instances.numAttributes()-1);
        NaiveBayes nb = new NaiveBayes();

		try {
			nb.buildClassifier(train_instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 // serialize model
		 ObjectOutputStream oos;
		 File temp = new File("/tmp/model");
		try {
			oos = new ObjectOutputStream(new FileOutputStream(temp));
			 oos.writeObject(nb);
			 oos.flush();
			 oos.close();
			 System.out.println("wrote in temp file");

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	
		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
//		AmazonS3 s3 = new AmazonS3Client(new ProfileCredentialsProvider());        
		try {
		    s3.putObject("lambda-bucket-arpit", "trained_nb", temp);
		    System.out.println("wrote in file s3");
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    System.exit(1);
		}

		ResponseClass output = new ResponseClass(nb.toString());
		System.out.println(output.getOutput());
        return output;
    }


}
