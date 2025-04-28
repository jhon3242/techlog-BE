package won.techlog.blog.domain

import won.techlog.poster.exception.NotFoundException

enum class BlogType(
    val baseUrl: String,
    val beanName: String
) {
    WOOWABRO("https://techblog.woowahan.com/", "woowabroBlogParser"),
    NAVER("https://d2.naver.com/helloworld", "naverBlogParser"),
    LINE("https://techblog.lycorp.co.jp/ko", "lineBlogParser"),
    KAKAO_PAY("https://tech.kakaopay.com/", "kakaoPayBlogParser"),
    COUPANG("https://medium.com/coupang-engineering/", "coupangBlogParser")
    ;

    companion object {
        fun getByUrl(url: String): BlogType =
            entries.find { url.startsWith(it.baseUrl) }
                ?: throw NotFoundException()
    }
}
