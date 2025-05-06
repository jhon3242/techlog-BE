package won.techlog.blog.domain.recommendation

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import won.techlog.common.BaseTimeEntity

@Entity
class BlogRecommendation(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val url: String,
    @Enumerated(EnumType.STRING)
    val recommendationStatus: BlogRecommendationStatus = BlogRecommendationStatus.PENDING,
    var isDeleted: Boolean = false
) : BaseTimeEntity()
