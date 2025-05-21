package won.techlog.blog.api.response

import won.techlog.blog.domain.recommendation.BlogRecommendation
import java.time.OffsetDateTime

data class BlogRecommendationResponse(
    val id: Long,
    val url: String,
    val status: String,
    val recommendedAt: OffsetDateTime
) {
    constructor(blogRecommendation: BlogRecommendation) : this(
        id = blogRecommendation.id,
        url = blogRecommendation.url,
        status = blogRecommendation.recommendationStatus.name,
        recommendedAt = blogRecommendation.createdAt
    )
}
