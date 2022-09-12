package com.pratap.accounts.restclients;

import com.pratap.accounts.model.Customer;
import com.pratap.accounts.model.Loans;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "myLoans", consumes = "application/json")
    ResponseEntity<List<Loans>> getLoansDetails(@RequestBody Customer customer);
}
