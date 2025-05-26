package won.techlog.poster.domain

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogType
import won.techlog.tag.domain.Tag
import java.time.OffsetDateTime

@Component
class PosterTagDao(
    private val posterTagRepository: PosterTagRepository
) {
    fun save(posterTag: PosterTag): PosterTag = posterTagRepository.save(posterTag)

    fun save(
        poster: Poster,
        tags: List<Tag>
    ): List<PosterTag> = tags.map { save(poster, it) }

    fun save(
        poster: Poster,
        tag: Tag
    ): PosterTag = posterTagRepository.save(PosterTag(poster = poster, tag = tag))

    fun findTags(poster: Poster): List<Tag> =
        posterTagRepository.findByPoster(poster)
            .filter { !it.tag.isDeleted }
            .map { it.tag }

    fun searchPosterTags(
        keyword: String? = null,
        blogType: BlogType? = null,
        tagNames: List<String>? = null,
        cursor: OffsetDateTime? = null
    ): List<PosterTag> = posterTagRepository.searchTop21(keyword, blogType, tagNames, cursor)

//    fun findByTagNames(tagNames: List<String>): List<PosterTag> =
//        posterTagRepository.findAllByTag_NameIn(tagNames)
//            .filter { !it.tag.isDeleted }
//            .distinct()
}
