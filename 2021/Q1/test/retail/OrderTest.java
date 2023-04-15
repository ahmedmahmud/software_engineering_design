package retail;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class OrderTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    CardProcessor cardProcessor = context.mock(CardProcessor.class);
    Courier courier = context.mock(Courier.class);
    List<Product> items = List.of(
        new Product("One Book", new BigDecimal("10.00")),
        new Product("One Book", new BigDecimal("10.00")),
        new Product("One Book", new BigDecimal("10.00")),
        new Product("One Book", new BigDecimal("10.00")),
        new Product("One Book", new BigDecimal("10.00")),
        new Product("One Book", new BigDecimal("10.00")));

    List<Product> moreItems = List.of(
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")),
            new Product("One Book", new BigDecimal("10.00")));
    CreditCardDetails card =  new CreditCardDetails("1234123412341234", 111);
    Address address = new Address("180 Queens Gate, London, SW7 2AZ");
    Courier fedex = new Fedex();

    class Fedex implements Courier {
        @Override
        public void send(Parcel shipment, Address shippingAddress) {
            System.out.println("Fedex will deliver your parcel to: " + shippingAddress);
        }

        @Override
        public BigDecimal deliveryCharge() {
            return new BigDecimal(1);
        }
    }

    @Test
    public void chargesSmallOrderForItsItemsCorrectly() {
        Order smallOrder = new SmallOrder(items, card, address, address, courier, false);

        context.checking(new Expectations() {{
            oneOf(courier).deliveryCharge(); will(returnValue(new BigDecimal(1)));
            oneOf(cardProcessor).charge(new BigDecimal("61.00"), card, address);
            oneOf(courier).send(with(any(Parcel.class)), with(address));
        }});

        smallOrder.process(cardProcessor);
    }

    @Test
    public void addsGiftWrapCostToSmallOrder() {
        Order smallOrder = new SmallOrder(items, card, address, address, courier, true);

        context.checking(new Expectations() {{
            oneOf(courier).deliveryCharge(); will(returnValue(new BigDecimal(1)));
            oneOf(cardProcessor).charge(new BigDecimal("64.00"), card, address);
            oneOf(courier).send(with(any(Parcel.class)), with(address));
        }});

        smallOrder.process(cardProcessor);
    }
    @Test
    public void chargesBulkOrderWithMoreSixToTenItemsCorrectly() {
        Order bulkOrder = new BulkOrder(items, card, address, address, courier, BigDecimal.ONE);

        context.checking(new Expectations() {{
            never(courier).deliveryCharge(); will(returnValue(new BigDecimal(1)));
            oneOf(cardProcessor).charge(new BigDecimal("53.00"), card, address);
            oneOf(courier).send(with(any(Parcel.class)), with(address));
        }});

        bulkOrder.process(cardProcessor);
    }

    @Test
    public void chargesBulkOrderWithFiveOrLessItemsCorrectly() {
        List<Product> lessItems = items.subList(0, 5);
        Order bulkOrder = new BulkOrder(lessItems, card, address, address, courier, BigDecimal.ONE);

        context.checking(new Expectations() {{
            never(courier).deliveryCharge(); will(returnValue(new BigDecimal(1)));
            oneOf(cardProcessor).charge(new BigDecimal("49.00"), card, address);
            oneOf(courier).send(with(any(Parcel.class)), with(address));
        }});

        bulkOrder.process(cardProcessor);
    }

    @Test
    public void chargesBulkOrderWithMoreThanTenItemsCorrectly() {
        Order bulkOrder = new BulkOrder(moreItems, card, address, address, courier, BigDecimal.ONE);

        context.checking(new Expectations() {{
            never(courier).deliveryCharge(); will(returnValue(new BigDecimal(1)));
            oneOf(cardProcessor).charge(new BigDecimal("87.00"), card, address);
            oneOf(courier).send(with(any(Parcel.class)), with(address));
        }});

        bulkOrder.process(cardProcessor);
    }
}
