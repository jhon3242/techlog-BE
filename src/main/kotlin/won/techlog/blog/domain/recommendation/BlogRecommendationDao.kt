package won.techlog.blog.domain.recommendation

import org.springframework.stereotype.Component

@Component
class BlogRecommendationDao(
    private val blogRecommendRepository: BlogRecommendationRepository
) {
    fun save(url: String): BlogRecommendation = blogRecommendRepository.save(BlogRecommendation(url = url))

    fun getById(id: Long): BlogRecommendation? =
        blogRecommendRepository.findByIdAndIsDeletedFalse(id)
            ?: throw IllegalArgumentException("일치하는 추천 블로그가 없습니다.")

    fun findAll(): List<BlogRecommendation> = blogRecommendRepository.findAllByIsDeletedFalse()

    fun delete(id: Long) {
        blogRecommendRepository.findByIdAndIsDeletedFalse(id)
            ?.let { it.isDeleted = true }
    }
}
