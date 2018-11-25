package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test_ClientService_getBalance {

    @Tested ClientService cs;
    @Injectable AccountService as;
    @Injectable EntityManager em;
    @Mocked Client c1;

    @Test
    void test_with_account(@Mocked Account a1) {

        List<Account> accountList = new ArrayList<>();
        accountList.add(a1);

        new Expectations() {{
           em.getClientAccounts(c1); result = accountList;
           a1.getBalance(); result = 100;
        }};

        assertEquals(100, cs.getBalance(c1));

    }

    @Test
    void test_base() {

        assertEquals(0, cs.getBalance(c1));

        new Verifications() {{
           em.getClientAccounts(c1); times = 1;
        }};

    }

}
