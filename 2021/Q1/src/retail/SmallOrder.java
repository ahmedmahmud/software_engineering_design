package retail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SmallOrder extends Order {
  private static final BigDecimal GIFT_WRAP_CHARGE = new BigDecimal(3);
  private final boolean giftWrap;

  public SmallOrder(
      List<Product> items,
      CreditCardDetails creditCardDetails,
      Address billingAddress,
      Address shippingAddress,
      Courier courier,
      boolean giftWrap) {
    super(items, creditCardDetails, billingAddress, shippingAddress, courier);
    this.giftWrap = giftWrap;
  }

  @Override
  BigDecimal costModifications(BigDecimal total) {
    total = total.add(courier.deliveryCharge());

    if (giftWrap) {
      total = total.add(GIFT_WRAP_CHARGE);
    }

    return total;
  }

  @Override
  void send() {
    if (giftWrap) {
      courier.send(new GiftBox(items), shippingAddress);
    } else {
      courier.send(new Parcel(items), shippingAddress);
    }
  }
}
