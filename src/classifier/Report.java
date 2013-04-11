/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author samuellouvan
 */
public class Report {
    
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scanner = new Scanner(new File("testing/netral.txt"));
        Scanner scanner2 = new Scanner(new File("testing/label_netral_result"));
        PrintWriter writer = new PrintWriter("results3.txt");
        int i = 0;
        while (i < 100)
        {
            writer.println(scanner.nextLine()+" "+scanner2.nextLine());
            writer.flush();
            i++;
        }
        writer.close();
        scanner.close();
        scanner2.close();
    }
}
