package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_ClientService_getRealBalance {

    @Tested ClientService cs;
    @Injectable AccountService as;
    @Injectable EntityManager em;
    @Mocked Client c1;

    @Test
    void test_base() {
        assertEquals(0, cs.getRealBalance(c1));
    }


    @Test
    void test_with_1(@Mocked Account a1, @Mocked CreditCard cc1) {

        List<Account> accountList = new ArrayList<>();
        List<CreditCard> creditCardList = new ArrayList<>();
        accountList.add(a1);
        creditCardList.add(cc1);

        new Expectations() {{
            em.getClientAccounts(c1); result = accountList;
            em.getCreditCards(a1); result = creditCardList;
            a1.getBalance(); result = 100;
            cc1.getCredit(); result = 99;
        }};

        assertEquals(1, cs.getRealBalance(c1));

        new Verifications() {{
           a1.getBalance(); times = 1;
           cc1.getCredit(); times = 1;
        }};

    }

    @Test
    void test_with_2(@Mocked Account a1, @Mocked CreditCard cc1) {

        List<Account> accountList = new ArrayList<>();
        List<CreditCard> creditCardList = new ArrayList<>();
        accountList.add(a1);
        creditCardList.add(cc1);

        new Expectations() {{
            em.getClientAccounts(c1); result = accountList;
            em.getCreditCards(a1); result = creditCardList;
            a1.getBalance(); result = 100;
            cc1.getCredit(); result = 98;
        }};

        assertEquals(2, cs.getRealBalance(c1));

        new Verifications() {{
            a1.getBalance(); times = 1;
            cc1.getCredit(); times = 1;
        }};

    }

}
