package won.techlog.blog.domain.crawler.rss

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class RssFeed(
    @JsonProperty("channel")
    val channel: Channel
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Channel(
    @JsonProperty("title")
    val title: String,
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    @JsonProperty("item")
    val items: List<RssItem>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RssItem(
    @JacksonXmlProperty(localName = "title")
    val title: String,
    @JacksonXmlProperty(localName = "link")
    val link: String,
    @JacksonXmlProperty(localName = "guid")
    val guid: String,
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "category")
    val categories: List<String> = emptyList(),
    @JacksonXmlProperty(
        localName = "creator",
        namespace = "http://purl.org/dc/elements/1.1/"
    )
    val creator: String? = null,
    @JacksonXmlProperty(localName = "pubDate")
    val pubDate: String,
    @JacksonXmlProperty(
        localName = "updated",
        namespace = "http://www.w3.org/2005/Atom"
    )
    val updated: String? = null,
    @JacksonXmlProperty(
        localName = "encoded",
        namespace = "http://purl.org/rss/1.0/modules/content/"
    )
    val content: String? = null
)
