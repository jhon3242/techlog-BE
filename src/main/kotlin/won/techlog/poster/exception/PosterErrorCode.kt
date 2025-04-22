package won.techlog.poster.exception

enum class PosterErrorCode(
    val code: String,
    val message: String
) {
    BLOG_NOT_FOUND_EXCEPTION("POS_001", "일치하는 블로그가 없습니다.")
}
