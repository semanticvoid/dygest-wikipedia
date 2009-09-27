/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dygest.twitter;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.SyndFeedInput;
import dygest.commons.data.Tweet;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anand
 */
public class TwitterSearch {

    private static final String BASE_URL = "http://search.twitter.com/search.atom?rpp=100&q=";
    private static final String LINK_FILTER = " +filter:links";
    private FeedFetcherCache feedInfoCache;
    private FeedFetcher feedFetcher;

    public TwitterSearch() {
        feedInfoCache = HashMapFeedInfoCache.getInstance();
        feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
    }

    public List<Tweet> search(String query, int max, boolean onlyLinks) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        String url = BASE_URL + query;

        if(max > 1500) {
            // limit imposed by twitter
            max = 1500;
        }

        if(onlyLinks) {
            url += LINK_FILTER;
        }

        try {
            URL feedUrl = new URL(url);
            SyndFeedInput input = new SyndFeedInput(false);
            SyndFeed feed = feedFetcher.retrieveFeed(feedUrl);

            List<SyndEntryImpl> entries = feed.getEntries();

            for(SyndEntryImpl entry : entries) {
                Tweet t = new Tweet(entry.getUri(), null, entry.getTitle(), entry.getPublishedDate(), true);
                tweets.add(t);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return tweets;
    }

    public static void main(String[] args) {
        TwitterSearch searcher = new TwitterSearch();
        List<Tweet> tweets = searcher.search("g20", 100, true);
        System.out.println(tweets.size());
    }

}
