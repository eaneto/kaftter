package kaftter.service;

import kaftter.factory.TweetFactory;
import kaftter.vo.Tweet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ExportTweetServiceTest {
    @Mock
    private TweetService tweetService;

    @InjectMocks
    private ExportTweetService exportTweetService;

    private TweetFactory tweetFactory;

    @Before
    public void setUp() {
        tweetFactory = new TweetFactory();
    }

    @Test
    public void generateCSvWithTenTweets() throws Exception {
        final int numberOfTweets = 10;
        final List<Tweet> tweets = tweetFactory.mockTweetsVo(numberOfTweets);
        when(tweetService.findTweets(numberOfTweets)).thenReturn(tweets);

        final Resource resource = exportTweetService.exportToCSV(numberOfTweets);

        assertThat(resource).isNotNull();
        assertThat(resource.getFilename()).contains("csv");
    }

}
