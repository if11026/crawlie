/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author samuellouvan
 */
public class FollowerLoader {
    private File file;
    
    public FollowerLoader(String fileName)
    {
        this.file = new File(fileName);
    }
    
    public ArrayList<Long> getFollowerIDs() throws FileNotFoundException
    {
        ArrayList<Long> ids = new ArrayList<Long>();
        
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
        {
            ids.add(Long.parseLong(scanner.nextLine()));
        }
        
        return ids;
    }
    
    public ArrayList<String> getFollowerScreenName() throws FileNotFoundException
    {
        ArrayList<String> ids = new ArrayList<String>();
        
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
        {
            ids.add(scanner.nextLine());
        }
        scanner.close();
        return ids;
    }
}
