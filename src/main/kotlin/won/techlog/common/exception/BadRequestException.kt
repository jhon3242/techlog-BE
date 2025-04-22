package won.techlog.common.exception

abstract class BadRequestException : RuntimeException() {
    abstract fun getErrorCode(): String
}
