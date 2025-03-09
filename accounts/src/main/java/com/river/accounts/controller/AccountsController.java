package com.river.accounts.controller;

import com.river.accounts.constants.AccountConstants;
import com.river.accounts.dto.*;
import com.river.accounts.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Tag(name = "Accounts Controller", description = "CRUD operations for accounts")
public class AccountsController {

    // Don't need @Autowired, because of constructor (lombok) injection
    private AccountService service;

    public AccountsController(AccountService service) {
        this.service = service;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    Environment env;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @Autowired
    private RiverDto riverDto;

    @GetMapping(path = "/fetch")
    @Operation(
            summary = "Get account",
            description = "Get account by mobile number")
    @ApiResponse(
            responseCode = "200",
            description = "HTTP status ok")
    public ResponseEntity<CustomerDto> getAccount(
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number is not valid")
            @Schema(example = "1234567890")
            String mobileNumber) {
        CustomerDto customerDto = service.getAccount(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @PostMapping(path = "/create")
    @Operation(
            summary = "Create new account",
            description = "Create new customer and account")
    @ApiResponse(
            responseCode = "201",
            description = "HTTP status created")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody @Valid CustomerDto dto) {
        service.createAccount(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201));
    }

    // When update account must send all fields
    @PatchMapping(path = "/update")
    @Operation(
            summary = "Update account",
            description = "Update customer and account")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "417",
                    description = "HTTP status expectation failed"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> updateAccount(@RequestBody @Valid CustomerDto dto) {
        boolean isUpdated = service.updateAccount(dto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping(path = "/delete")
    @Operation(
            summary = "Delete account",
            description = "Delete customer and account")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "417",
                    description = "HTTP status expectation failed"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number is not valid") String mobileNumber) {
        boolean isDeleted = service.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_DELETE));
        }
    }

    @GetMapping(path = "/build-info")
    @Operation(
            summary = "Get build information",
            description = "Get build information")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @GetMapping(path = "/java-version")
    @Operation(
            summary = "Get java version",
            description = "Get java version")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(env.getProperty("JAVA_HOME"));
    }

    @GetMapping(path = "/contact-info")
    @Operation(
            summary = "Get contact information",
            description = "Get contact information")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<AccountsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }

    @GetMapping(path = "/river-info")
    @Operation(
            summary = "Get river information",
            description = "Get river information")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP status ok"),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP status internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<RiverDto> getRiverInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(riverDto);
    }

}
