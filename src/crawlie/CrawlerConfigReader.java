/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crawlie;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author samuellouvan
 */
public class CrawlerConfigReader {

    static CrawlerConfigReader conf = new CrawlerConfigReader("crawlerConfig.txt");
    private String fileName;
    static final int CONSUMER_KEY = 0;
    static final int CONSUMER_SECRET = 1;
    static final int ACCESS_TOKEN = 2;
    static final int ACCESS_TOKEN_SECRET = 3;
    
    public CrawlerConfigReader(String fileName) {
        this.fileName = fileName;
    }

    public static CrawlerConfigReader getInstance() {
        return conf;
    }

    public Configuration readConfiguration(int ID) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        Configuration conf = null;
        int cnt = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (cnt == ID) {
                String[] confStrings = line.split(";");
                conf = new Configuration(confStrings[CONSUMER_KEY], confStrings[CONSUMER_SECRET], confStrings[ACCESS_TOKEN], confStrings[ACCESS_TOKEN_SECRET]);
            }
            cnt++;
        }
        return conf;
    }
}

class Configuration {
    private final String proxyHost = "152.118.24.10";
    private final int proxyPort = 8080;
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    public Configuration(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }
    
    
}
