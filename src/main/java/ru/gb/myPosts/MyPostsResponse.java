package ru.gb.myPosts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data",
        "meta"
})
@Data
public class MyPostsResponse {
    @JsonProperty("data")
    public List<PostsData> data = null;
    @JsonProperty("meta")
    public Meta meta;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "id",
            "title",
            "description",
            "content",
            "authorId",
            "mainImage",
            "updatedAt",
            "createdAt",
            "labels",
            "delayPublishTo",
            "draft"
    })
    @Data
    public static class PostsData {
        @JsonProperty("id")
        public Integer id;
        @JsonProperty("title")
        public String title;
        @JsonProperty("description")
        public String description;
        @JsonProperty("content")
        public String content;
        @JsonProperty("authorId")
        public Integer authorId;
        @JsonProperty("mainImage")
        public MainImage mainImage;
        @JsonProperty("updatedAt")
        public String updatedAt;
        @JsonProperty("createdAt")
        public String createdAt;
        @JsonProperty("labels")
        public List<Object> labels = null;
        @JsonProperty("delayPublishTo")
        public Object delayPublishTo;
        @JsonProperty("draft")
        public Boolean draft;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonPropertyOrder({
                "id",
                "cdnUrl"
        })
        @Data
        public static class MainImage {
            @JsonProperty("id")
            public Integer id;
            @JsonProperty("cdnUrl")
            public String cdnUrl;
            @JsonIgnore
            private Map<String, Object> additionalProperties = new HashMap<String, Object>();
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "prevPage",
            "nextPage",
            "count"
    })
    @Data
    public static class Meta {
        @JsonProperty("prevPage")
        public String prevPage;
        @JsonProperty("nextPage")
        public String nextPage;
        @JsonProperty("count")
        public Integer count;
        @JsonIgnore
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    }
}