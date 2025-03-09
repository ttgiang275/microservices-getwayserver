package com.river.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Response", description = "Response success information")
public class ResponseDto {

    @Schema(description = "Status code")
    private String statusCode;

    @Schema(name = "Status message", description = "Status message")
    private String statusMsg;

}
