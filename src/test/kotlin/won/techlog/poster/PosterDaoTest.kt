package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.PosterTag
import won.techlog.support.BaseServiceTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture

class PosterDaoTest : BaseServiceTest() {
    @Test
    fun `키워드로 제목 검색한다`() {
        // given
        posterDao.savePoster(PosterFixture.create(title = "redis test"))

        // when
        val result = posterDao.searchTop21Posters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `키워드로 내용 검색한다`() {
        // given
        posterDao.savePoster(PosterFixture.create(content = "redis test"))

        // when
        val result = posterDao.searchTop21Posters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `키워드로 태그 검색한다`() {
        // given
        val savedPoster = posterDao.savePoster(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result = posterDao.searchTop21Posters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `회사 이름으로 검색한다`() {
        // given
        // when
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.LINE))
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.WOOWABRO))
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.WOOWABRO))

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
        posterDao.savePoster(PosterFixture.create(title = "redis test"))
        posterDao.savePoster(PosterFixture.create(content = "redis test"))
        val savedPoster = posterDao.savePoster(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result = posterDao.searchTop21Posters("No")

        // then
        Assertions.assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `상위 21개만 조회된다`() {
        // given
        for (idx in 0..50) {
            posterDao.savePoster(PosterFixture.create(blogType = BlogType.WOOWABRO))
        }

        // when
        val result = posterDao.searchTop21Posters(blogType = BlogType.WOOWABRO)

        // then
        Assertions.assertThat(result).hasSize(21)
    }
}
