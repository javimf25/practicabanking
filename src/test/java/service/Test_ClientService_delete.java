package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.exceptions.AccountActive;
import org.udg.caes.banking.exceptions.ClientNotFound;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

public class Test_ClientService_delete {

    @Tested ClientService cs;
    @Injectable AccountService as;
    @Injectable EntityManager em;
    @Mocked Client c1;


    @Test
    void test_ok(@Mocked Account a1) throws ClientNotFound, NotEnoughBalance, PersistenceException, AccountActive {

        List<Account> accountList = new ArrayList<>();
        accountList.add(a1);

        new Expectations() {{
            cs.getClient("c1"); result = c1;
            em.getClientAccounts(c1); result = accountList;
        }};

        cs.delete("c1");

        new Verifications() {{
           as.delete(a1);
        }};
    }

}
