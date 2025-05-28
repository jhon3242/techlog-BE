package won.techlog.blog.domain.client.company.daangn

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import won.techlog.blog.domain.BlogMetaData
import won.techlog.common.TimeProvider

@JsonIgnoreProperties(ignoreUnknown = true)
data class DaangnBlogContentsResponse(
    val success: Boolean,
    val payload: DaangnPayload
) {
    fun getBlogMetaData(): List<BlogMetaData> {
        return payload.references.Post.values.map { post ->
            BlogMetaData(
                title = post.title,
                content = post.subtitle ?: post.content.subtitle ?: post.virtuals.subtitle ?: "",
                thumbnailUrl =
                    "https://miro.medium.com/v2/resize:fit:1400/format:webp/" +
                        "${post.virtuals.previewImage?.imageId}",
                url = post.uniqueSlug,
                publishedAt = TimeProvider.parseByLong(post.firstPublishedAt)
            )
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DaangnPayload(
        val references: DaangnReferences
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DaangnReferences(
        val Post: Map<String, DaangnPost>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DaangnPost(
        val id: String,
        val title: String,
        val creatorId: String,
        val homeCollectionId: String,
        val createdAt: Long,
        val updatedAt: Long,
        val firstPublishedAt: Long,
        val latestPublishedAt: Long,
        val content: Content,
        val virtuals: Virtuals,
        val previewContent: PreviewContent?,
        val slug: String,
        val subtitle: String?,
        val uniqueSlug: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Content(
        val subtitle: String?,
        val postDisplay: PostDisplay?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PostDisplay(
        val coverless: Boolean
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Virtuals(
        val subtitle: String?,
        val previewImage: PreviewImage?,
        val recommends: Int,
        val wordCount: Int,
        val readingTime: Double,
        val tags: List<Tag>,
        val responsesCreatedCount: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PreviewImage(
        val imageId: String,
        val originalWidth: Int,
        val originalHeight: Int
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Tag(
        val slug: String,
        val name: String,
        val postCount: Int,
        val metadata: TagMetadata
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class TagMetadata(
        val postCount: Int,
        val coverImage: CoverImage?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CoverImage(
        val id: String,
        val originalWidth: Int?,
        val originalHeight: Int?,
        val isFeatured: Boolean?,
        val alt: String?
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PreviewContent(
        val bodyModel: BodyModel
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BodyModel(
        val paragraphs: List<Paragraph>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Paragraph(
        val type: Int,
        val text: String
    )
}
