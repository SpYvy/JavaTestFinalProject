package ru.gb.finalProject;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "error",
        "code",
        "message"
})
@Data
public class AuthorizationBadCredentialsQueryResponse {

    @JsonProperty("error")
    public String error;
    @JsonProperty("code")
    public Integer code;
    @JsonProperty("message")
    public String message;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}