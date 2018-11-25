package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_CreditCardService_charge {

    @Tested CreditCardService ccs;
    @Injectable EntityManager em;
    long amount = 100;


    @Test
    void test_ok(@Mocked Account a1, @Mocked CreditCard cc1) throws PersistenceException, NotEnoughBalance {

        new Expectations() {{
            a1.getBalance(); result = amount+1;
            cc1.getCredit(); result = amount;
        }};

        ccs.charge(cc1, a1);

        new VerificationsInOrder() {{
            a1.debit(amount); times = 1;
            em.persist(a1); times = 1;
        }};

        new VerificationsInOrder() {{
            cc1.reset(); times = 1;
            em.persist(cc1); times = 1;
        }};

    }

    @Test
    void test_no_balance(@Mocked Account a1, @Mocked CreditCard cc1) throws PersistenceException {

        new Expectations() {{
            a1.getBalance(); result = amount;
            cc1.getCredit(); result = amount+1;
        }};

        assertThrows(NotEnoughBalance.class, () -> ccs.charge(cc1, a1));

        new Verifications() {{
            em.persist(a1); times=0;
            em.persist(cc1); times=0;
        }};
    }


}
