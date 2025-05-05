package won.techlog.poster

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PostersCreateRequest
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagsFixture

private const val BASE_URL = "/api/posters"
private const val ADMIN_HEADER = "X-Admin-Header"

class AdminControllerTest : BaseControllerTest() {
    @Value("\${admin.header}")
    lateinit var adminHeaderKey: String

    @Test
    fun `어드민 권한이 없으면 요청에 실패한다`() {
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
            .post("/api/poster")
            .then().log().all()
            .statusCode(500)
    }

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
}
