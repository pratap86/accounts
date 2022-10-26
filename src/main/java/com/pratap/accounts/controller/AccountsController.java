package com.pratap.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.pratap.accounts.config.AccountsServiceConfig;
import com.pratap.accounts.model.Accounts;
import com.pratap.accounts.model.Cards;
import com.pratap.accounts.model.CustomerDetails;
import com.pratap.accounts.model.Loans;
import com.pratap.accounts.model.Properties;
import com.pratap.accounts.repository.AccountsRepository;
import com.pratap.accounts.restclients.CardsFeignClient;
import com.pratap.accounts.restclients.LoansFeignClient;
import com.pratap.accounts.util.AccountsUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Slf4j
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private AccountsServiceConfig accountsServiceConfig;

    @Autowired
    private CardsFeignClient cardsFeignClient;

    @Autowired
    private LoansFeignClient loansFeignClient;

    @GetMapping("/myAccount")
    public ResponseEntity<Accounts> getAccountDetails(@RequestParam int customerId) {

        Accounts accounts = accountsRepository.findByCustomerId(customerId);
        if (accounts != null)
            return new ResponseEntity<Accounts>(accounts, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/properties")
    public String getPropertiesDetails() throws JsonProcessingException {

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
                accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());
        return objectWriter.writeValueAsString(properties);
    }

    @GetMapping("/myCustomerDetails")
    @CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod = "myCustomerDetailsFallBack")
    public ResponseEntity<CustomerDetails> getCustomerDetails(@RequestHeader("narayanbank-correlation-id") String correlationId,
            @RequestParam int customerId){

        log.info("Executing getCustomerDetails() with customerId = {}", customerId);
        Accounts accounts = accountsRepository.findByCustomerId(customerId);
        List<Loans> loansDetails = loansFeignClient.getLoansDetails(correlationId, customerId).getBody();
        List<Cards> cardDetails = cardsFeignClient.getCardDetails(correlationId, customerId).getBody();

        CustomerDetails customerDetails = new CustomerDetails();
        if (accounts != null)
            customerDetails.setAccounts(accounts);
        if (loansDetails != null && !loansDetails.isEmpty())
            customerDetails.setLoans(loansDetails);
        if (cardDetails != null && !cardDetails.isEmpty())
            customerDetails.setCards(cardDetails);
        log.info("Completed getCustomerDetails()");
        return new ResponseEntity<CustomerDetails>(customerDetails, HttpStatus.OK);
    }

    private ResponseEntity<CustomerDetails> myCustomerDetailsFallBack(@RequestHeader("narayanbank-correlation-id") String correlationId,
                                                                      int customerId, Throwable throwable){

        Accounts accounts = accountsRepository.findByCustomerId(customerId);
        List<Loans> loansDetails = loansFeignClient.getLoansDetails(correlationId, customerId).getBody();
        List<Cards> cards = new ArrayList<>();
        cards = AccountsUtil.jsonToJava("cards.json", Cards.class);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loansDetails);
        customerDetails.setCards(cards);
        return new ResponseEntity<CustomerDetails>(customerDetails, HttpStatus.OK);
    }
}
