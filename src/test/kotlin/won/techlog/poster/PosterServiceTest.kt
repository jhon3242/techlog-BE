package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.BlogType
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.domain.PosterService
import won.techlog.poster.domain.PosterTag
import won.techlog.support.BaseServiceTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture

class PosterServiceTest : BaseServiceTest() {
    @Autowired
    lateinit var posterService: PosterService

    @Test
    fun `키워드로 검색한다`() {
        // given
        val request = PosterSearchRequest(keyword = "redis")
        posterDao.savePoster(PosterFixture.create(title = "redis test"))
        posterDao.savePoster(PosterFixture.create(content = "redis test"))
        val savedPoster = posterDao.savePoster(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result =
            posterService.searchPosters(request)
                .posters

        // then
        Assertions.assertThat(result).hasSize(3)
    }

    @Test
    fun `일치하는 키워드가 없으면 검색되지 않는다`() {
        // given
        val request = PosterSearchRequest(keyword = "NoMatch")
        posterDao.savePoster(PosterFixture.create(title = "redis test"))
        posterDao.savePoster(PosterFixture.create(content = "redis test"))
        val savedPoster = posterDao.savePoster(PosterFixture.create())
        val savedTag = tagDao.save(TagFixture.create(name = "redis"))
        posterTagDao.save(PosterTag(poster = savedPoster, tag = savedTag))

        // when
        val result =
            posterService.searchPosters(request)
                .posters

        // then
        Assertions.assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `회사 이름으로 검색한다`() {
        // given
        val request = PosterSearchRequest(blogType = BlogType.LINE.name)
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.LINE))
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.WOOWABRO))
        posterDao.savePoster(PosterFixture.create(blogType = BlogType.WOOWABRO))

        // when
        val result =
            posterService.searchPosters(request)
                .posters

        // then
        Assertions.assertThat(result).hasSize(1)
    }
}
