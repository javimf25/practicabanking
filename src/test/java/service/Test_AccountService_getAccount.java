package org.udg.caes.banking.service;

import mockit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.AccountNotFound;
import org.udg.caes.banking.exceptions.EntityNotFound;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class Test_AccountService_getAccount {

    @Tested AccountService as;
    @Injectable EntityManager em;
    Account a1;

    @BeforeEach
    void setup() {
        a1 = new Account("id1", 100);
    }

    @Test
    void test_ok() throws Exception{

        new Expectations() {{
            em.get(a1.getId(), Account.class); result=a1;
        }};

        assertEquals(a1,as.getAccount(a1.getId()));

    }

    @Test
    void test_no_account_exception() throws EntityNotFound {

        new Expectations() {{
            em.get(a1.getId(), Account.class); result=new EntityNotFound();
        }};

        try {
            as.getAccount(a1.getId());
            fail("");
        } catch (AccountNotFound accountNotFound) {

        }
    }
}
