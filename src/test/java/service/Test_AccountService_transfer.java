package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.AccountNotFound;
import org.udg.caes.banking.exceptions.EntityNotFound;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class Test_AccountService_transfer {

    @Tested AccountService as;
    @Injectable EntityManager em;
    Account a1;
    Account a2;
    static long amount;

    @BeforeAll
    static void setup() {
        amount = 100;
    }

    void test_ok() throws NotEnoughBalance, PersistenceException, AccountNotFound, EntityNotFound {

        a1 = new Account("id1", amount);
        a2 = new Account("id2", 0);
        new Expectations() {{
            em.get(a1.getId(), Account.class); result=a1;
            em.get(a2.getId(), Account.class); result=a2;
        }};

        as.transfer(a1.getId(), a2.getId(), amount);

        new Verifications() {{
           em.persist(a1); times=1;
           em.persist(a2); times=1;
        }};

        assertEquals(0, a1.getBalance());
        assertEquals(amount, a2.getBalance());

    }

    @Test
    void test_no_account_from() throws AccountNotFound, PersistenceException, NotEnoughBalance {

        a2 = new Account("id2", 0);
        new Expectations() {{
           as.getAccount(""); result= new AccountNotFound();
           as.getAccount(a2.getId()); result= a2; minTimes=0;
        }};

        try {
            as.transfer("", a2.getId(), amount);
            fail("");
        } catch (AccountNotFound e) {

        }

        new Verifications() {{
            em.persist(any); times=0;
        }};

        assertEquals(0, a2.getBalance());

    }

    @Test
    void test_no_account_to() throws AccountNotFound, PersistenceException {

        a1 = new Account("id1", amount);


        new Expectations() {{
            as.getAccount(a1.getId()); result = a1; minTimes = 0;
            as.getAccount(anyString); result= new AccountNotFound();
        }};

        assertThrows(AccountNotFound.class, () -> as.transfer(a1.getId(), "", amount));

        new Verifications() {{
           em.persist(any); times = 0;
        }};

        assertEquals(amount, a1.getBalance());

    }

    @Test
    void test_no_balance_from() throws AccountNotFound, PersistenceException {

        a1 = new Account("id1", amount);
        a2 = new Account("id2", 0);
        new Expectations() {{
           as.getAccount("id1"); result = a1;
           as.getAccount("id2"); result = a2; minTimes = 0;
        }};

        try {
            as.transfer(a1.getId(), a2.getId(), amount+1);
            fail("");
        } catch (NotEnoughBalance e) {

        }

        new Verifications() {{
            em.persist(any); times=0;
        }};

        assertEquals(0, a2.getBalance());

    }
}
