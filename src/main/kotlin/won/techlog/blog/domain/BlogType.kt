package won.techlog.blog.domain

import won.techlog.poster.exception.NotFoundException

enum class BlogType(
    val baseUrl: String
) {
    WOOWABRO("https://techblog.woowahan.com/"),
    NAVER("https://d2.naver.com/helloworld"),
    LINE("https://techblog.lycorp.co.jp/ko"),
    LINE_OLD("https://engineering.linecorp.com/ko/blog/"),
    KAKAO_PAY("https://tech.kakaopay.com/"),
    KAKAO("https://tech.kakao.com/"),
    COUPANG("https://medium.com/coupang-engineering/"),
    TOSS("https://toss.tech/"),
    DAANGN("https://medium.com/daangn"),
    WATCHA("https://medium.com/watcha/"),
    MUSINSA("https://medium.com/musinsa/"),
    ZIGBANG("https://medium.com/zigbang/"),
    MEGAZONE_CLOUD("https://medium.com/cloudvillains/"),
    YANOLJA_CLOUD("https://medium.com/yanoljacloud-tech/"),
    WANTED("https://medium.com/wantedjobs/"),
    NAVER_PLACE("https://medium.com/naver-place-dev/"),
    STYLE_SHARE("https://medium.com/styleshare/"),
    NHN("https://meetup.nhncloud.com/"),
    NONE("None")
    ;

    companion object {
        fun getByUrl(url: String): BlogType =
            entries.find { url.startsWith(it.baseUrl) }
                ?: throw NotFoundException()

        fun getByName(name: String): BlogType =
            entries.find { it.name == name }
                ?: throw NotFoundException()

        fun findByName(name: String?): BlogType? = entries.find { it.name == name }
    }
}
