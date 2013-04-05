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
        FollowerLoader loader = new FollowerLoader("idFollowerAnakUI.txt");
        ArrayList<Long> ids = loader.getFollowerIDs();
        int totalID = ids.size();
        Thread[] downloaders = new Thread[10];

        int nbFollowerPerThread = totalID / downloaders.length;
        int residue = totalID % downloaders.length;
        for (int i = 0; i < downloaders.length; i++) {
            TweetDownloader obj = new TweetDownloader(i, true);
            obj.addUserIDS(ids);
            if (i == downloaders.length - 1) {
                obj.setStartIdx(i * nbFollowerPerThread);
                obj.setEndIdx(i * nbFollowerPerThread + nbFollowerPerThread + residue-1);
            } else {
                obj.setStartIdx(i * nbFollowerPerThread);
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
