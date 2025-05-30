package won.techlog.blog.domain.client.company.toss

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.common.TimeProvider

@JsonIgnoreProperties(ignoreUnknown = true)
data class TossBlogContentsResponse(
    val resultType: String,
    val error: String?,
    val success: SuccessData
) {
    fun getBlogMetaDataList(): List<BlogMetaData> {
        return success.results.map { post ->
            if (post.shortDescription.isNullOrBlank() && post.fullDescription.isNullOrBlank()) {
                return@map handleSimplicity(post)
            }
            BlogMetaData(
                title = post.title,
                thumbnailUrl = post.thumbnailConfig?.imageUrl,
                content = post.shortDescription ?: post.fullDescription ?: "",
                url = "https://toss.tech/article/${post.key}",
                publishedAt = TimeProvider.parseByString(post.publishedTime)
            )
        }
    }

    private fun handleSimplicity(post: BlogPost): BlogMetaData {
        return BlogMetaData(
            title = post.title,
            thumbnailUrl = post.thumbnailConfig?.imageUrl,
            content = post.subtitle,
            url = post.bottomButtonConfig?.landingUrl ?: "https://toss.tech/article/${post.key}",
            publishedAt = TimeProvider.parseByString(post.publishedTime)
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SuccessData(
        val page: Int,
        val pageSize: Int,
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: List<BlogPost>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BlogPost(
        val id: Long,
        val updatedTime: String,
        val createdTime: String,
        val category: String,
        val categories: List<Category>,
        val title: String,
        val subtitle: String,
        val bottomButtonConfig: BottomButtonConfig?,
        val coverConfig: CoverConfig?,
        val thumbnailConfig: ThumbnailConfig?,
        val key: String,
        val publishedTime: String,
        val likeCount: Int,
        val seoConfig: SeoConfig?,
        val openGraph: OpenGraph?,
        val editor: Editor?,
        val shortDescription: String?,
        val fullDescription: String?,
        val viewCount: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Category(
        val name: String,
        val id: Int,
        val slug: String?,
        val parentId: Int?,
        val iconUrl: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BottomButtonConfig(
        val ctaType: String?,
        val imageUrl: String?,
        val imageAlt: String?,
        val title: String?,
        val description: String?,
        val ctaName: String?,
        val landingUrl: String?,
        val isSearchBottomCta: Boolean?,
        val landingInExternalBrowser: Boolean?,
        val serviceId: String?,
        val stockConfig: String?,
        val landingScheme: String?,
        val landingExternal: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CoverConfig(
        val coverType: String,
        val imageUrl: String?,
        val videoUrl: String?,
        val backgroundColor: String?,
        val isFill: Boolean,
        val imageAlt: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ThumbnailConfig(
        val imageUrl: String?,
        val backgroundColor: String?,
        val isFill: Boolean,
        val imageAlt: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class SeoConfig(
        val post: Long,
        val description: String?,
        val urlSlug: String?,
        val primaryKeyword: Keyword?,
        val relatedKeywords: List<Keyword>?,
        val tags: List<Keyword>?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Keyword(
        val id: Long,
        val content: String,
        val wordType: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class OpenGraph(
        val title: String?,
        val description: String?,
        val backgroundColor: String?,
        val imageAlt: String?,
        val imageUrl: String?,
        val imageType: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Editor(
        val id: Int,
        val name: String?,
        val slug: String?,
        val shortDescription: String?,
        val description: String?,
        val imageUrl: String?,
        val postCount: Int
    )
}
