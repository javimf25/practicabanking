package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.NotEnoughBalance;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_CreditCardService_delete {

    @Tested CreditCardService ccs;
    @Injectable EntityManager em;
    Account a1;
    CreditCard cc1;

    @Test
    void test_ok() throws PersistenceException, NotEnoughBalance {
        a1 = new Account("a1", 100);
        cc1 = new CreditCard("cc1");

        new Expectations() {{
           em.getAccountAssociated(cc1); result = a1;
        }};
        ccs.delete(cc1);
        new VerificationsInOrder() {{
            ccs.charge(cc1,a1); times=1;
            em.delete(cc1); times=1;
        }};
    }


    @Test
    void test_no_account_associated() throws PersistenceException, NotEnoughBalance {

        cc1 = new CreditCard("cc1");

        new Expectations() {{
            em.getAccountAssociated(cc1); result = null;
        }};

        assertThrows(NullPointerException.class, () -> ccs.delete(cc1));

        new Verifications() {{
            em.delete(cc1); times = 0;
        }};

    }
}
