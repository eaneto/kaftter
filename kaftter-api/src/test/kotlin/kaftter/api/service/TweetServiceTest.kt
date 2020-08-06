package kaftter.api.service

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kaftter.api.domain.SummarizedTweetEntity
import kaftter.api.exception.UserNotFoundException
import kaftter.api.repository.SummarizedTweetRepository
import kaftter.api.repository.TweetRepository
import kaftter.api.vo.Tweet
import kaftter.api.vo.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockKExtension::class)
class TweetServiceTest {
    @MockK
    private lateinit var tweetRepository: TweetRepository

    @MockK
    private lateinit var summarizedTweetRepository: SummarizedTweetRepository

    @InjectMockKs
    private lateinit var tweetService: TweetService

    @Test
    fun `test saving tweet on database should call repository once`() {
        val tweet = mockTweet()
        every { tweetRepository.save(any()!!) } returns mockk()

        tweetService.save(tweet)

        verify { tweetRepository.save(any()!!) }
    }

    @Test
    fun `test searching for tweet summary of non existing user should throw not found exception`() {
        val userId = 1L
        every { summarizedTweetRepository.findByUserId(userId) } returns Optional.empty()

        assertThrows<UserNotFoundException> { tweetService.search(userId) }

        verify { summarizedTweetRepository.findByUserId(userId) }
    }

    @Test
    fun `test searching for tweet summary of existing user should return user summary`() {
        val userId = 1L
        val summarizedTweetEntity = mockk<SummarizedTweetEntity>()
        every { summarizedTweetRepository.findByUserId(userId) } returns Optional.of(summarizedTweetEntity)

        val tweetSummary = tweetService.search(userId)

        verify { summarizedTweetRepository.findByUserId(userId) }
        assertThat(tweetSummary).isEqualTo(summarizedTweetEntity)
    }

    private fun mockTweet(): Tweet {
        return Tweet(
                id = 1L,
                text = "test",
                favoriteCount = 1,
                replyCount = 1,
                quoteCount = 2,
                retweetCount = 0,
                language = "en",
                createdAt = LocalDateTime.now(),
                user = User(
                        id = 2,
                        followers = 0,
                        name = "none",
                        screenName = "none"
                )
        )
    }
}