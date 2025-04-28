package won.techlog.poster

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PostersCreateRequest
import won.techlog.poster.api.response.PosterResponse
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagsFixture

private const val BASE_URL = "/api/posters"
private const val ADMIN_HEADER = "X-Admin-Header"

class PosterControllerTest : BaseControllerTest() {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Value("\${admin.header}")
    lateinit var adminHeaderKey: String

    @Test
    fun `포스터를 추가한다`() {
        // given
        val poster = PosterFixture.create()
        val tagNames = TagsFixture.create()
        tagNames.forEach { tagDao.save(it) }
        val request =
            PosterCreateRequest(
                title = poster.blogMetaData.title,
                thumbnail = poster.blogMetaData.thumbnailUrl,
                url = poster.blogMetaData.url,
                content = poster.blogMetaData.content,
                blogType = poster.blogType.name,
                tags = tagNames
            )

        // when
        // then
        RestAssured.given().log().all()
            .body(request)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(ADMIN_HEADER, adminHeaderKey)
            .post("/api/poster")
            .then().log().all()
            .statusCode(201)
    }

    @Test
    fun `포스터 리스트를 추가한다`() {
        // given
        val poster = PosterFixture.create()
        val tagNames = TagsFixture.create()
        tagNames.forEach { tagDao.save(it) }
        val posterA =
            PosterCreateRequest(
                title = poster.blogMetaData.title + "A",
                thumbnail = poster.blogMetaData.thumbnailUrl,
                url = poster.blogMetaData.url,
                content = poster.blogMetaData.content,
                blogType = poster.blogType.name,
                tags = tagNames
            )
        val posterB =
            PosterCreateRequest(
                title = poster.blogMetaData.title + "B",
                thumbnail = poster.blogMetaData.thumbnailUrl,
                url = poster.blogMetaData.url,
                content = poster.blogMetaData.content,
                blogType = poster.blogType.name,
                tags = tagNames
            )
        val request = PostersCreateRequest(listOf(posterA, posterB))

        // when
        // then
        RestAssured.given().log().all()
            .body(request)
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .header(ADMIN_HEADER, adminHeaderKey)
            .post("/api/posters")
            .then().log().all()
            .statusCode(201)
    }

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
    fun `모든 포스터를 조회한다`() {
        // given

        val posterA = PosterFixture.create(title = "test A")
        val posterB = PosterFixture.create(title = "test B")
        posterDao.savePoster(posterA)
        posterDao.savePoster(posterB)

        // when
        val result =
            RestAssured.given().log().all()
                .queryParam("page", 0)
                .queryParam("size", 10)
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
            .header(ADMIN_HEADER, adminHeaderKey)
            .`when`().delete("$BASE_URL/{id}")
            .then().log().all()
            .statusCode(200)
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
