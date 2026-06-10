//package com.example.whitefox.common.response;

package com.example.whitefox.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private Boolean success;

    private String message;

    private T data;
}