package won.techlog.poster

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.BlogType
import won.techlog.poster.api.request.PosterSearchRequest
import won.techlog.poster.api.request.PosterUpdateRequest
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
        posterDao.save(PosterFixture.create(title = "redis test"))
        posterDao.save(PosterFixture.create(content = "redis test"))
        posterDao.save(PosterFixture.create(content = "Nothing test"))

        // when
        val result =
            posterService.searchPosters(request)
                .posters

        // then
        Assertions.assertThat(result).hasSize(2)
    }

    @Test
    fun `일치하는 키워드가 없으면 검색되지 않는다`() {
        // given
        val request = PosterSearchRequest(keyword = "NoMatch")
        posterDao.save(PosterFixture.create(title = "redis test"))
        posterDao.save(PosterFixture.create(content = "redis test"))
        val savedPoster = posterDao.save(PosterFixture.create())
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
        posterDao.save(PosterFixture.create(blogType = BlogType.LINE))
        posterDao.save(PosterFixture.create(blogType = BlogType.WOOWABRO))
        posterDao.save(PosterFixture.create(blogType = BlogType.WOOWABRO))

        // when
        val result =
            posterService.searchPosters(request)
                .posters

        // then
        Assertions.assertThat(result).hasSize(1)
    }

    @Test
    fun `포스터의 제목을 수정한다`() {
        // given
        val savePoster = posterDao.save(PosterFixture.create())
        val updateValue = "update title"
        val request = PosterUpdateRequest(title = updateValue)

        // when
        posterService.updatePoster(savePoster.id, request)
        val result = posterService.getPoster(savePoster.id)

        // then
        Assertions.assertThat(result.title).isEqualTo(updateValue)
    }

    @Test
    fun `포스터의 썸네일을 수정한다`() {
        // given
        val savePoster = posterDao.save(PosterFixture.create())
        val updateValue = "update thumbnail"
        val request = PosterUpdateRequest(thumbnail = updateValue)

        // when
        posterService.updatePoster(savePoster.id, request)
        val result = posterService.getPoster(savePoster.id)

        // then
        Assertions.assertThat(result.thumbnail).isEqualTo(updateValue)
    }

    @Test
    fun `포스터의 url을 수정한다`() {
        // given
        val savePoster = posterDao.save(PosterFixture.create())
        val updateValue = "update url"
        val request = PosterUpdateRequest(url = updateValue)

        // when
        posterService.updatePoster(savePoster.id, request)
        val result = posterService.getPoster(savePoster.id)

        // then
        Assertions.assertThat(result.url).isEqualTo(updateValue)
    }

    @Test
    fun `포스터의 블로그 타입을 수정한다`() {
        // given
        val savePoster = posterDao.save(PosterFixture.create())
        val updateValue = BlogType.WOOWABRO
        val request = PosterUpdateRequest(blogType = updateValue.name)

        // when
        posterService.updatePoster(savePoster.id, request)
        val result = posterService.getPoster(savePoster.id)

        // then
        Assertions.assertThat(result.blogType).isEqualTo(updateValue.name)
    }
}
