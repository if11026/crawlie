/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.Classifier;
import weka.core.Instances;

/**
 *
 * @author samuellouvan
 */
public class TestClassifier {

    public static void main(String[] args) throws Exception {
        Classifier cs = (Classifier) weka.core.SerializationHelper.read("bayes-sentiment-3000-multinomial.model");
        // load unlabeled data
        Instances unlabeled = new Instances(
                new BufferedReader(
                new FileReader("testing/netral.arff")));

        // set class attribute
        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

        // create copy
        Instances labeled = new Instances(unlabeled);

        // label instances
        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = cs.classifyInstance(unlabeled.instance(i));
            
            labeled.instance(i).setClassValue(clsLabel);
            System.out.println(clsLabel);
        }
        // save labeled data
        BufferedWriter writer = new BufferedWriter(new FileWriter("testing/netral_labeled.arff"));
        writer.write(labeled.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }
}
