package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.PosterTag
import won.techlog.support.BaseServiceTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture
import java.time.OffsetDateTime

class PosterDaoTest : BaseServiceTest() {
    @Test
    fun `키워드로 제목 검색한다`() {
        // given
        posterDao.save(PosterFixture.create(title = "redis test"))
        posterDao.save(PosterFixture.create(title = "Nothing test"))

        // when
        val result = posterDao.searchTop21Posters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `키워드로 내용 검색한다`() {
        // given
        posterDao.save(PosterFixture.create(content = "redis test"))
        posterDao.save(PosterFixture.create(content = "Nothing test"))

        // when
        val result = posterDao.searchTop21Posters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `태그 검색한다`() {
        // given
        val savedPoster = posterDao.save(PosterFixture.create())
        val savedPoster2 = posterDao.save(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result = posterDao.searchTop21Posters(tagNames = listOf("redis"))

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `회사 이름으로 검색한다`() {
        // given
        // when
        posterDao.save(PosterFixture.create(blogType = BlogType.LINE))
        posterDao.save(PosterFixture.create(blogType = BlogType.WOOWABRO))
        posterDao.save(PosterFixture.create(blogType = BlogType.WOOWABRO))

        // then
        assertAll(
            { Assertions.assertThat(posterDao.searchTop21Posters(blogType = BlogType.LINE)).hasSize(1) },
            { Assertions.assertThat(posterDao.searchTop21Posters(blogType = BlogType.WOOWABRO)).hasSize(2) },
            { Assertions.assertThat(posterDao.searchTop21Posters(blogType = BlogType.NAVER)).hasSize(0) }
        )
    }

    @Test
    fun `일치하는 키워드가 없으면 조회되지 않는다`() {
        // given
        posterDao.save(PosterFixture.create(title = "redis test"))
        posterDao.save(PosterFixture.create(content = "redis test"))
        val savedPoster = posterDao.save(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result = posterDao.searchTop21Posters("No")

        // then
        Assertions.assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `날짜 내림차순으로 상위 21개만 조회된다`() {
        // given
        for (idx in 0..50) {
            posterDao.save(
                PosterFixture.create(
                    blogType = BlogType.WOOWABRO,
                    publishedAt = OffsetDateTime.now().minusDays(idx.toLong())
                )
            )
        }

        // when
        val result = posterDao.searchTop21Posters()

        // then
        assertAll(
            {
                Assertions.assertThat(
                    result.get(0).blogMetaData.publishedAt.dayOfYear
                ).isEqualTo(OffsetDateTime.now().minusDays(0).dayOfYear)
            },
            {
                Assertions.assertThat(
                    result.get(1).blogMetaData.publishedAt.dayOfYear
                ).isEqualTo(OffsetDateTime.now().minusDays(1).dayOfYear)
            },
            {
                Assertions.assertThat(
                    result.get(2).blogMetaData.publishedAt.dayOfYear
                ).isEqualTo(OffsetDateTime.now().minusDays(2).dayOfYear)
            }
        )
    }
}
