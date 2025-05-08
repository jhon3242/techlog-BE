package won.techlog.blog.domain.client.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * API 응답 전체를 매핑하기 위한 DTO
 */
data class KakaoBlogContentsResponse(
    val contents: List<KakaoBlogContentResponse>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoBlogContentResponse(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val releaseDateTime: String,
    val categories: List<Category>,
    val author: Author,
    val authors: List<Author>,
    val thumbnailUri: String,
    val tags: List<Tag>,
    val content: String,
    // 아래 필드는 JSON이 null이거나 없으면 기본값(null) 사용
    val components: Any? = null,
    val prevPostTitleResponse: NextPostTitleResponse? = null,
    val nextPostTitleResponse: NextPostTitleResponse? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    val code: String,
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Author(
    val name: String,
    val description: String,
    val profile: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tag(
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NextPostTitleResponse(
    val id: Int,
    val title: String
)
