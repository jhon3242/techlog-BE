package won.techlog.blog.domain

import org.springframework.data.jpa.repository.JpaRepository

interface BlogRecommendationRepository : JpaRepository<BlogRecommendation, Long> {
    fun findAllByIsDeletedFalse(): List<BlogRecommendation>

    fun findByIdAndIsDeletedFalse(id: Long): BlogRecommendation?
}
