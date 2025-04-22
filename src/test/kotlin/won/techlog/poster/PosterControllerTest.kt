package won.techlog.poster

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.poster.api.response.PosterResponse
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.PosterFixture

private const val BASE_URL = "/api/posters"

class PosterControllerTest: BaseControllerTest() {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `특정 포스터를 조회한다`() {
        // given
        val poster = PosterFixture.create()
        val savedPoster = posterDao.savePoster(poster)

        // when
        val posterResponse = RestAssured.given().log().all()
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
            { Assertions.assertThat(posterResponse.thumbnail).isEqualTo(savedPoster.blogMetaData.thumbnailUrl) },
        )
    }

    @Test
    fun `모든 포스터를 조회한다`() {
        // given

        val posterA = PosterFixture.create(title = "test A")
        val posterB = PosterFixture.create(title = "test B")
        posterDao.savePoster(posterA)
        posterDao.savePoster(posterB)

        // when
        val result = RestAssured.given().log().all()
            .`when`().get(BASE_URL)
            .then().log().all()
            .statusCode(200)
            .extract()
            .response()
            .body

        // TODO 리스트를 List<Object>로 매핑

    }

    @Test
    fun `포스터를 삭제한다`() {
        // given
        val poster = PosterFixture.create()
        val savePoster = posterDao.savePoster(poster)

        // when & then
        RestAssured.given().log().all()
            .pathParam("id", savePoster.id)
            .`when`().delete("$BASE_URL/{id}")
            .then().log().all()
            .statusCode(200)
    }
}
