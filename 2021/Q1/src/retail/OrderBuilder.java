package retail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderBuilder {
    private final List<Product> productList = new ArrayList<>();
    private Address billing;
    private Address shipping;
    private boolean giftWrapped = false;
    private BigDecimal discount = BigDecimal.ZERO;

    private CreditCardDetails creditCard;
    private Courier courier;

    public OrderBuilder addProduct(Product product) {
        this.productList.add(product);
        return this;
    }

    public OrderBuilder addProducts(List<Product> products) {
        this.productList.addAll(products);
        return this;
    }

    public OrderBuilder setBilling(Address address) {
        this.billing = address;
        return this;
    }

    public OrderBuilder setShipping(Address address) {
        this.shipping = address;
        return this;
    }

    public OrderBuilder giftWrap() {
        this.giftWrapped = true;
        return this;
    }

    public OrderBuilder setDiscount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public OrderBuilder setCreditCard(CreditCardDetails creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public OrderBuilder setCourier(Courier courier) {
        this.courier = courier;
        return this;
    }

    public Order build() {
        Order order;

        if (shipping == null) {
            shipping = billing;
        }

        if (productList.size() > 3) {
            order = new BulkOrder(productList, creditCard, billing, shipping, courier, discount);
        } else {
            order = new SmallOrder(productList, creditCard, billing, shipping, courier, giftWrapped);
        }

        return order;
    }
}
