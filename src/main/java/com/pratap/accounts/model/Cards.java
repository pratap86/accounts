package com.pratap.accounts.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class Cards {

    private int cardId;
    private int customerId;
    private String cardNumber;
    private String cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
    private Date createDt;
}
