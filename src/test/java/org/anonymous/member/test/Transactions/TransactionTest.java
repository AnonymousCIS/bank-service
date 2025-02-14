package org.anonymous.member.test.Transactions;

import org.anonymous.bank.services.transaction.TransactionsUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
public class TransactionTest {

    @Autowired
    private TransactionsUpdateService transactionsUpdateService;


    @Test
    void test1() {
        for (int i = 0; i < 10; i++) {
            transactionsUpdateService.updateTransactions();
        }
    }
}
