package won.techlog.support.fixture

import won.techlog.blog.domain.BlogRecommendation

object BlogRecommendationFixture {
    fun create(url: String = "https://tech.kakaopay.com/page/1/"): BlogRecommendation = BlogRecommendation(url = url)
}
