package won.techlog.blog.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class BlogRecommendation(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
    val url: String,
    var isDeleted: Boolean = false
)
