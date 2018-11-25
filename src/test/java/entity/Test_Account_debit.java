package org.udg.caes.banking.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Account;
import org.udg.caes.banking.exceptions.NotEnoughBalance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_Account_debit {

    Account Exemple;
    long amount = 100;

    @BeforeEach
    void setup() {
        Exemple = new Account("id1", amount);
    }

    @Test
    void test_ok() throws NotEnoughBalance {
        Exemple.debit(amount);
        assertEquals(0, Exemple.getBalance());
    }

    @Test
    void test_not_enough_balance() {
        assertThrows(NotEnoughBalance.class, () -> Exemple.debit(amount+1));
    }


}
