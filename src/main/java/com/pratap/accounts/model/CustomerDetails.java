package com.pratap.accounts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CustomerDetails {

    private Accounts accounts;
    private List<Loans> loans;
    private List<Cards> cards;
}
