package retail;

import java.math.BigDecimal;

public interface CardProcessor {
    void charge(BigDecimal amount, CreditCardDetails account, Address billingAddress);
}
