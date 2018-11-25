package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.AccountActive;
import org.udg.caes.banking.exceptions.AccountNotFound;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_AccountService_delete {

    @Tested AccountService as;
    @Injectable EntityManager em;
    @Injectable CreditCardService ccs;
    @Mocked Account Exemple;
    

    @Test
    void test_ok() throws AccountActive, PersistenceException, AccountNotFound, NotEnoughBalance {

        new Expectations() {{
            Exemple.getBalance(); result = 0;
            as.getAccount("id1"); result = Exemple;
        }};

        as.delete("id1");

        new VerificationsInOrder() {{
            Exemple.getBalance(); times = 1;
            em.getCreditCards(Exemple); times = 1;
            em.delete(Exemple); times = 1;
        }};
    }

    @Test
    void test_account_active() throws AccountNotFound, NotEnoughBalance, PersistenceException, AccountActive {

        new Expectations() {{
            Exemple.getBalance(); result = 100;
            as.getAccount("id1"); result = Exemple;
        }};

        assertThrows(AccountActive.class, () -> as.delete("id1"));

        new Verifications() {{
            em.delete(Exemple); times=0;
        }};
    }


    @Test
    void test_account_creditcard_active(@Mocked CreditCard cc1) throws AccountNotFound, NotEnoughBalance, PersistenceException {


        List<CreditCard> cl = new ArrayList<>();
        cl.add(cc1);

        new Expectations() {{
            cc1.getCredit(); result = 1;
            as.getAccount("id1"); result = Exemple;
            em.getCreditCards(Exemple);  result=cl;
        }};

        assertThrows(AccountActive.class, () -> as.delete("id1"));

        new Verifications() {{
            em.delete(cc1); times=0;
            em.delete(Exemple); times=0;
        }};

    }
}
