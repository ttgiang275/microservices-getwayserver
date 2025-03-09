package com.river.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Customer", description = "Customer details")
public class CustomerDto {

    @NotEmpty(message = "Name can not be null or empty")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    @Schema(name = "name", description = "Name of the customer", example = "John Doe")
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email is not valid")
    @Schema(description = "Email of the customer", example = "john@example.com")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number is not valid")
    @Schema(description = "Mobile number of the customer", example = "1234567890")
    private String mobileNumber;

    @Schema(name = "account", description = "Account details of the customer")
    private AccountDto account;

}
