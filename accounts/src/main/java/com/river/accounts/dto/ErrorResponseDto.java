package com.river.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Error Response", description = "Response for error")
public class ErrorResponseDto {

    @Schema(description = "API path", example = "/accounts")
    private String apiPath;

    @Schema(description = "Error code")
    private HttpStatus errorCode;

    @Schema(description = "Error message")
    private String errorMessage;

    @Schema(description = "Error time", example = "2023-01-01T00:00:00")
    private LocalDateTime errorTime;

}
