package won.techlog.poster

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import won.techlog.blog.domain.BlogType
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PostersCreateRequest
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.PosterFixture
import won.techlog.support.fixture.TagFixture

private const val BASE_URL = "/api/posters"

class AdminControllerTest : BaseControllerTest() {
    @Test
    fun `어드민 권한이 없으면 요청에 실패한다`() {
        // given
        val poster = PosterFixture.create()
        val tagNames = TagFixture.createList()
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
        val poster =
            PosterFixture.create(
                title = "신뢰성 향상을 위한 SLI/SLO 도입 1편 - 소개와 필요성",
                thumbnail =
                    "https://vos.line-scdn.net/landpress-content-v2-vcfc68aynwenkh3bno0ixfx8/" +
                        "5042567b7bc641a4aa857e6bdf79d769.png?updatedAt=1739853211000",
                content =
                    "시작하며 안녕하세요. SRE(site reliability engineering, 사이트 안정성 엔지니어링) 업무를 맡고 있는 " +
                        "Enablement Engineering 팀 어다희, Service Reliability 팀 천기철입니다. " +
                        "저희 두 팀은 Service Engineering " +
                        "실에 속해 있으며, LINE 앱에서 제공하는 서비스의 품질 향상 및 가용성 확보를 위한 기술 활동을 수행합니다. " +
                        "보다 구체적으로는 " +
                        "메시징 서비스와 미디어 플랫폼 등 LINE 앱 서비스에 대한 SRE을 담당하고 있습니다. 이를 위해 서비스 출시 및 이벤트",
                url = "https://techblog.lycorp.co.jp/ko/sli-and-slo-for-improving-reliability-1",
                blogType = BlogType.LINE
            )
        val tagNames = TagFixture.createList()
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
            .header(Companion.ADMIN_HEADER, adminHeaderKey)
            .post("/api/poster")
            .then().log().all()
            .statusCode(201)
    }

    @Test
    fun `포스터 리스트를 추가한다`() {
        // given
        val poster = PosterFixture.create()
        val tagNames = TagFixture.createList()
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
            .header(Companion.ADMIN_HEADER, adminHeaderKey)
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
            .header(Companion.ADMIN_HEADER, adminHeaderKey)
            .`when`().delete("$BASE_URL/{id}")
            .then().log().all()
            .statusCode(200)
    }
}
