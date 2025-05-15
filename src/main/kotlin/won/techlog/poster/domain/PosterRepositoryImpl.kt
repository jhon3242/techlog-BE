package won.techlog.poster.domain

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.QPoster.poster
import won.techlog.poster.domain.QPosterTag.posterTag
import won.techlog.tag.domain.QTag.tag
import java.time.LocalDateTime

class PosterRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : PosterRepositoryCustom {
    override fun searchPosters(
        keyword: String?,
        tagNames: List<String>?,
        blogType: String?,
        cursorCreatedAt: LocalDateTime?,
        cursorId: Long?,
        size: Int
    ): List<Poster> {
        val query =
            queryFactory.selectFrom(poster)
                .distinct()
                .leftJoin(posterTag).on(poster.id.eq(posterTag.poster.id))
                .leftJoin(tag).on(posterTag.tag.id.eq(tag.id))
                .where(
                    buildKeywordCondition(keyword),
                    blogType?.let { poster.blogType.eq(BlogType.valueOf(it)) },
                    tagNames?.takeIf { it.isNotEmpty() }?.let { tag.name.`in`(it) }
                )

        return query.fetch()
    }

    private fun buildKeywordCondition(keyword: String?): BooleanExpression? {
        if (keyword.isNullOrBlank()) return null

        val keywords = keyword.trim().split("\\s+".toRegex())
        var condition: BooleanExpression? = null

        for (word in keywords) {
            val wordCondition =
                poster.blogMetaData.title.containsIgnoreCase(word)
                    .or(poster.blogMetaData.content.containsIgnoreCase(word))
                    .or(tag.name.containsIgnoreCase(word))

            condition = condition?.and(wordCondition) ?: wordCondition
        }

        return condition
    }

    private fun buildCursorCondition(
        createdAt: LocalDateTime?,
        id: Long?
    ): BooleanExpression? {
        if (createdAt == null || id == null) return null

//        return poster.createdAt.lt(createdAt)
//            .or(poster.createdAt.eq(createdAt).and(poster.id.lt(id)))
        return null
    }
}
