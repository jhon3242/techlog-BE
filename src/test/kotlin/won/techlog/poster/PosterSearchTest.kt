package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import won.techlog.poster.domain.PosterTag
import won.techlog.support.BaseServiceTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture

class PosterSearchTest: BaseServiceTest() {
    @Test
    fun `키워드로 제목 검색한다`() {
        // given
        posterDao.savePoster(PosterFixture.create(title = "redis test"))

        // when
        val result = posterDao.searchPosters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `키워드로 내용 검색한다`() {
        // given
        posterDao.savePoster(PosterFixture.create(content = "redis test"))

        // when
        val result = posterDao.searchPosters("redis")

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
        val result = posterDao.searchPosters("redis")

        // then
        Assertions.assertThat(result.size).isEqualTo(1)
    }
}
