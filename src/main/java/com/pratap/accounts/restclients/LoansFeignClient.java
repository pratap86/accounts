package com.pratap.accounts.restclients;

import com.pratap.accounts.model.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "loans/myLoans", consumes = "application/json")
    ResponseEntity<List<Loans>> getLoansDetails(@RequestHeader("narayanbank-correlation-id") String correlationId,
                                                @RequestParam int customerId);
}
