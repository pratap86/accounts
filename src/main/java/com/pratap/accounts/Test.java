package com.pratap.accounts;

import com.pratap.accounts.model.Cards;
import com.pratap.accounts.util.AccountsUtil;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Cards> cards = new ArrayList<>();
        cards = AccountsUtil.jsonToJava("cards.json", Cards.class);
        System.out.println(cards);
    }
}
