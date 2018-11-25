package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.CreditCard;
import org.udg.caes.banking.exceptions.PersistenceException;
import org.udg.caes.banking.manager.EntityManager;

public class Test_CreditCardService_invalidate {

    @Tested CreditCardService ccs;
    @Injectable EntityManager em;


    @Test
    void test_ok(@Mocked CreditCard cc1) throws PersistenceException {

        ccs.invalidate(cc1);

        new VerificationsInOrder() {{
           cc1.setActive(false);
           em.persist(cc1);
        }};

    }

}
