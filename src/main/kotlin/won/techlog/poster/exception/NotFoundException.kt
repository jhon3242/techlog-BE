package won.techlog.poster.exception

import won.techlog.common.exception.BadRequestException

class NotFoundException : BadRequestException() {
    override fun getErrorCode(): String = PosterErrorCode.BLOG_NOT_FOUND_EXCEPTION.code
}
