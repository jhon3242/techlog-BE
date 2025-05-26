package won.techlog.poster

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.poster.api.response.PosterResponse
import won.techlog.poster.domain.PosterTagDao
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.PosterFixture

private const val BASE_URL = "/api/posters"

class PosterControllerTest : BaseControllerTest() {
    @Autowired
    private lateinit var posterTagDao: PosterTagDao

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `특정 포스터를 조회한다`() {
        // given
        val poster = PosterFixture.create()
        val savedPoster = posterDao.savePoster(poster)

        // when
        val posterResponse =
            RestAssured.given().log().all()
                .pathParam("id", 1)
                .`when`().get("$BASE_URL/{id}")
                .then().log().all()
                .statusCode(200)
                .extract().`as`(PosterResponse::class.java)

        // then
        org.junit.jupiter.api.Assertions.assertAll(
            { Assertions.assertThat(posterResponse.url).isEqualTo(savedPoster.blogMetaData.url) },
            { Assertions.assertThat(posterResponse.title).isEqualTo(savedPoster.blogMetaData.title) },
            { Assertions.assertThat(posterResponse.content).isEqualTo(savedPoster.blogMetaData.content) },
            { Assertions.assertThat(posterResponse.thumbnail).isEqualTo(savedPoster.blogMetaData.thumbnailUrl) }
        )
    }

    @Test
    fun `포스터를 추천한다`() {
        // given
        val poster = PosterFixture.create()
        val savePoster = posterDao.savePoster(poster)

        // when
        RestAssured.given().log().all()
            .pathParam("id", savePoster.id)
            .`when`().put("$BASE_URL/{id}/recommend")
            .then().log().all()
            .statusCode(200)

        // then
        val curPoster = posterDao.getPoster(savePoster.id)
        Assertions.assertThat(curPoster.recommendations).isGreaterThan(0)
    }

    @Test
    fun `포스터를 조회한다`() {
        // given
        val poster = PosterFixture.create()
        val savePoster = posterDao.savePoster(poster)

        // when
        RestAssured.given().log().all()
            .pathParam("id", savePoster.id)
            .`when`().put("$BASE_URL/{id}/view")
            .then().log().all()
            .statusCode(200)

        // then
        val curPoster = posterDao.getPoster(savePoster.id)
        Assertions.assertThat(curPoster.views).isGreaterThan(0)
    }
}
