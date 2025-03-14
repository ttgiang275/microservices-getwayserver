package com.river.accounts.service.client;

import com.river.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping(path = "/api/fetch", consumes = "application/json", produces = "application/json")
    ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader String correlationId, @RequestParam String mobileNumber);

}
