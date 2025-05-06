package won.techlog.blog

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.Test
import won.techlog.blog.api.request.BlogRecommendationRequest
import won.techlog.support.BaseControllerTest
import won.techlog.support.fixture.BlogRecommendationFixture

class BlogRecommendTest : BaseControllerTest() {
    @Test
    fun `블로그를 추천한다`() {
        // given
        val request = BlogRecommendationRequest(url = "https://tech.kakaopay.com/page/1/")

        // when & then
        RestAssured.given().log().all()
            .body(request)
            .contentType(ContentType.JSON)
            .`when`().post("/api/blogs/recommendations")
            .then().log().all()
            .statusCode(201)
    }

    @Test
    fun `추천 블로그를 모두 조회한다`() {
        // given
        val blogRecommendation = BlogRecommendationFixture.create()
        blogRecommendationDao.save(blogRecommendation.url)

        // when & then
        RestAssured.given().log().all()
            .`when`().get("/api/blogs/recommendations")
            .then().log().all()
            .statusCode(200)
    }

    @Test
    fun `추천 블로그를 삭제한다`() {
        // given
        val blogRecommendation = BlogRecommendationFixture.create()
        blogRecommendationDao.save(blogRecommendation.url)

        // when & then
        RestAssured.given().log().all()
            .pathParam("id", blogRecommendation.id)
            .`when`().delete("/api/blogs/recommendations/{id}")
            .then().log().all()
            .statusCode(200)
    }
}
