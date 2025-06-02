package won.techlog.poster.domain

import org.springframework.stereotype.Component
import won.techlog.tag.domain.Tag

@Component
class PosterTagDao(
    private val posterTagRepository: PosterTagRepository
) {
    fun save(posterTag: PosterTag): PosterTag = posterTagRepository.save(posterTag)

    fun save(
        poster: Poster,
        tags: List<Tag>
    ): List<PosterTag> {
        val existingPosterTags = posterTagRepository.findByPosterAndIsDeletedFalse(poster)
        val existingTagIds = existingPosterTags.map { it.tag.id }
        val newTagIds = tags.map { it.id }

        val tagsToDelete = existingTagIds - newTagIds.toSet()
        existingPosterTags
            .filter { it.tag.id in tagsToDelete }
            .forEach { it.isDeleted = true }

        val tagsToAdd = newTagIds - existingTagIds.toSet()
        val newPosterTags =
            tags
                .filter { it.id in tagsToAdd }
                .map { PosterTag(poster = poster, tag = it) }

        return posterTagRepository.saveAll(newPosterTags).toList()
    }

    fun save(
        poster: Poster,
        tag: Tag
    ): PosterTag =
        posterTagRepository.findByPoster_IdAndTag_IdAndIsDeletedFalse(poster.id, tag.id)
            ?: posterTagRepository.save(PosterTag(poster = poster, tag = tag))

    fun findTags(poster: Poster): List<Tag> =
        posterTagRepository.findByPosterAndIsDeletedFalse(poster)
            .filter { !it.tag.isDeleted }
            .map { it.tag }

    fun findPosters(tag: Tag): List<Poster> =
        posterTagRepository.findAllByTag_IdAndIsDeletedFalse(tag.id)
            .map { it.poster }
}
