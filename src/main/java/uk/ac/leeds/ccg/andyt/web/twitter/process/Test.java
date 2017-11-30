/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.web.twitter.process;

//import twitter4j.api;
//import twitter4j.auth.;
//import twitter4j.conf.;
//import twitter4j.json.;
//import twitter4j.management.;
//import twitter4j.util;
//import twitter4j.util.function.;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RawStreamListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_ReadCSV;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_StaticIO;

public class Test implements StatusListener {

    /**
     * Main entry of this application.
     *
     * @param args arguments doesn't take effect with this example
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public static void main(String[] args) throws TwitterException {

        List<String> l = searchtweets();
        Iterator<String> ite;
        ite = l.iterator();
        while (ite.hasNext()) {
            System.out.println(ite.next());
        }
//        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
//        RawStreamListener listener = new RawStreamListener() {
//            @Override
//            public void onMessage(String rawJSON) {
//                System.out.println(rawJSON);
//            }
//
//            @Override
//            public void onException(Exception ex) {
//                ex.printStackTrace();
//            }
//        };
//        twitterStream.addListener(listener);
//        twitterStream.sample();

    }

    static Twitter getTwitterinstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        File d = new File(System.getProperty("user.dir"), "data");
        d = new File(d, "twitter");
        d = new File(d, "config");
        File f = new File(d, "twitter4j.properties");
        ArrayList<String> lines = Generic_ReadCSV.read(f, f.getParentFile(), 7);
        Iterator<String> ite;
        ite = lines.iterator();
        String consumerKey = ite.next().replace("oauth.consumerKey", "");
        String consumerSecret = ite.next().replace("oauth.consumerSecret", "");
        String accessToken = ite.next().replace("oauth.accessToken", "");
        String accessTokenSecret = ite.next().replace("oauth.accessTokenSecret", "");
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }

    public static List<String> searchtweets() throws TwitterException {

        ArrayList<String> result;
        result = new ArrayList<>();
        Twitter twitter = getTwitterinstance();
        //Query query = new Query("source:twitter4j agdturner");
//        Query query = new Query("test");
//        QueryResult result = twitter.search(query);

        try {
            Query query = new Query("source:twitter4j agdturner");
            //Query query = new Query("Test");
            QueryResult queryResult;
            queryResult = twitter.search(query);
            List<Status> tweets = queryResult.getTweets();
            for (Status tweet : tweets) {
                result.add("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }

//        return result.getTweets().stream()
//                .map(item -> item.getText())
//                .collect(Collectors.toList());
        return result;
    }

    @Override
    public void onStatus(Status status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice sdn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onStallWarning(StallWarning sw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onException(Exception excptn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
