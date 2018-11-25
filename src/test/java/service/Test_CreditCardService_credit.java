package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.CreditExceeded;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_CreditCardService_credit {

    @Tested CreditCardService ccs;
    @Injectable EntityManager em;
    CreditCard c1;


    @Test
    void test_ok(@Mocked CreditCard cc1) throws PersistenceException, CreditExceeded {

        long amount = 100, creditv = 1;
        new Expectations() {{
           cc1.getCredit(); result = amount-creditv;
           cc1.getMaxCredit(); result = amount;
        }};

        ccs.credit(cc1, creditv);

        new VerificationsInOrder() {{
           cc1.credit(creditv);
           em.persist(cc1);
        }};

    }


    @Test
    void test_credit_exceeded(@Mocked CreditCard cc1) throws PersistenceException, CreditExceeded {

        long amount = 100, creditv = 1;
        new Expectations() {{
           cc1.getCredit(); result = amount;
           cc1.getMaxCredit(); result = amount-creditv;
        }};

        assertThrows(CreditExceeded.class, () -> ccs.credit(cc1, creditv));

        new Verifications() {{
           cc1.credit(anyLong); times = 0;
           em.persist(cc1); times = 0;
        }};

    }
}
