/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlie;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 *
 * @author samuellouvan
 */
public class TweetDownloaderRunner {

    public static void main(String[] args) throws FileNotFoundException {
        /*FollowerLoader loader = new FollowerLoader("idFollowerAnakUI.txt");
        ArrayList<Long> ids = loader.getFollowerIDs();
        int totalID = ids.size();
        Thread[] downloaders = new Thread[10];

        int nbFollowerPerThread = totalID / downloaders.length;
        int residue = totalID % downloaders.length;
        for (int i = 0; i < downloaders.length; i++) {
            TweetDownloader obj = new TweetDownloader(i, true);
            obj.addUserIDS(ids);
            obj.setStartIdx(i * nbFollowerPerThread);
            if (i == downloaders.length - 1) {
                obj.setEndIdx(i * nbFollowerPerThread + nbFollowerPerThread + residue-1);
            } else {
                obj.setEndIdx(i * nbFollowerPerThread + nbFollowerPerThread-1);
            }
            System.out.println("Thread "+i+" starts from "+obj.getStartIdx() +" ends at "+obj.getEndIdx());
            downloaders[i] = new Thread(obj);
        }

        for (int i = 0; i < downloaders.length; i++) {
            downloaders[i].start();
        }*/
        
        
        FollowerLoader loader = new FollowerLoader("twitterNewsID.txt");
        ArrayList<String> screenNames = loader.getFollowerScreenName();
        int totalID = screenNames.size();
        Thread[] downloaders = new Thread[1];

        int nbFollowerPerThread = totalID / downloaders.length;
        int residue = totalID % downloaders.length;
        for (int i = 0; i < downloaders.length; i++) {
            TweetDownloader obj = new TweetDownloader(i, true);
            obj.setScreenNames(screenNames);
            obj.setStartIdx(i * nbFollowerPerThread);
            if (i == downloaders.length - 1) {
                obj.setEndIdx(i * nbFollowerPerThread + nbFollowerPerThread + residue-1);
            } else {
                obj.setEndIdx(i * nbFollowerPerThread + nbFollowerPerThread-1);
            }
            System.out.println("Thread "+i+" starts from "+obj.getStartIdx() +" ends at "+obj.getEndIdx());
            downloaders[i] = new Thread(obj);
        }

        for (int i = 0; i < downloaders.length; i++) {
            downloaders[i].start();
        }
    }
}
