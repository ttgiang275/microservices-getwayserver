package com.river.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Account", description = "Account details")
public class AccountDto {

    @NotEmpty(message = "Account number can not be null or empty")
    @Schema(name = "Account number", description = "Account number", example = "1")
    private Integer accountNumber;

    @NotEmpty(message = "Account type can not be null or empty")
    @Schema(name = "Account type", description = "Account type", example = "Savings")
    private String accountType;

    @NotEmpty(message = "Branch address can not be null or empty")
    @Schema(name = "Branch address", description = "Branch address", example = "123 Main Street - New York")
    private String branchAddress;

}
