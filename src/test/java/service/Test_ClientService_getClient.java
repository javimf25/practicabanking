package org.udg.caes.banking.service;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import org.junit.jupiter.api.Test;
import org.udg.caes.banking.entity.Client;
import org.udg.caes.banking.exceptions.ClientNotFound;
import org.udg.caes.banking.exceptions.EntityNotFound;
import org.udg.caes.banking.manager.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Test_ClientService_getClient {

    @Tested ClientService cs;
    @Injectable EntityManager em;
    @Mocked Client c1;

    @Test
    void test_ok() throws EntityNotFound, ClientNotFound {

        new Expectations() {{
           em.get("c1", Client.class); result = c1;
        }};

        assertEquals(c1, cs.getClient("c1"));

    }

    @Test
    void test_no_client_exception() throws EntityNotFound, ClientNotFound {

        new Expectations() {{
            em.get("c1", Client.class); result = new EntityNotFound();
        }};

        assertThrows(ClientNotFound.class, () -> cs.getClient("c1"));

    }

}
