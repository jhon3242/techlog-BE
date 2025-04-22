package won.techlog.poster

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.poster.api.response.PosterResponse
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.BlogMetaDataFixture

private const val BASE_URL = "/api/posters"

class PosterControllerTest: BaseControllerTest() {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `특정 포스터를 조회한다`() {
        // given
        val blogMetaData = BlogMetaDataFixture.create()
        val savedPoster = posterDao.savePoster(blogMetaData)

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
        val blogMetaDataA = BlogMetaDataFixture.create(title = "test A")
        val blogMetaDataB = BlogMetaDataFixture.create(title = "test B")
        posterDao.savePoster(blogMetaDataA)
        posterDao.savePoster(blogMetaDataB)

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
        val blogMetaData = BlogMetaDataFixture.create()
        posterDao.savePoster(blogMetaData)

        // when & then
        RestAssured.given().log().all()
            .pathParam("id", 1)
            .`when`().delete("$BASE_URL/{id}")
            .then().log().all()
            .statusCode(200)
    }
}
