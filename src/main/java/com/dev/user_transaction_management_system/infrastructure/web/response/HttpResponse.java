package com.dev.user_transaction_management_system.infrastructure.web.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {
    protected String timestamps;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected String path;
    protected String requestMethod;
    protected Object data;
}
