package com.pratap.accounts.restclients;

import com.pratap.accounts.model.Cards;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping(value = "cards/myCards", consumes = "application/json")
    ResponseEntity<List<Cards>> getCardDetails(@RequestParam int customerId);
}
