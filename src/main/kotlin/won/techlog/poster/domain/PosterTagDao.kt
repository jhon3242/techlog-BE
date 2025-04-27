package won.techlog.poster.domain

import org.springframework.stereotype.Component
import won.techlog.tag.domain.Tag

@Component
class PosterTagDao(
    private val posterTagRepository: PosterTagRepository
) {
    fun save(posterTag: PosterTag): PosterTag
    = posterTagRepository.save(posterTag)

    fun save(poster: Poster, tags: List<Tag>): List<PosterTag>
    = tags.map { save(poster, it) }

    fun save(poster: Poster, tag: Tag): PosterTag
    = posterTagRepository.save(PosterTag(poster = poster, tag = tag))

    fun findTags(poster: Poster): List<Tag> =
        posterTagRepository.findByPoster(poster)
            .map { it.tag }
}
