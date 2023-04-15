package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

public abstract class Order {
    final List<Product> items;
    final CreditCardDetails creditCardDetails;
    final Address billingAddress;
    final Address shippingAddress;
    final Courier courier;
    public Order(
            List<Product> items,
            CreditCardDetails creditCardDetails,
            Address billingAddress,
            Address shippingAddress,
            Courier courier) {
        this.items = Collections.unmodifiableList(items);
        this.creditCardDetails = creditCardDetails;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.courier = courier;
    }

    public void process(CardProcessor cardProcessor) {
        BigDecimal total = new BigDecimal(0);

        for (Product item : items) {
            total = total.add(item.unitPrice());
        }

        total = costModifications(total);

        cardProcessor.charge(round(total), creditCardDetails, billingAddress);

        send();
    }
    abstract BigDecimal costModifications(BigDecimal total);

    abstract void send();

    private BigDecimal round(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.CEILING);
    }
}
