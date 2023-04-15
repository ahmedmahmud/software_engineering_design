package ic.doc;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AuctionManagerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  PaymentSystem paymentSystem = context.mock(PaymentSystem.class);
  Dispatcher dispatcher = context.mock(Dispatcher.class);
  @Test
  public void acceptsInitialBid() {
    AuctionManager auctionManager = new AuctionManager(paymentSystem, dispatcher);

    context.checking(new Expectations() {{
      exactly(1).of(paymentSystem).charge(10, "alice");
    }});

    auctionManager.startAuction("clock", "bob");
    assertThat(auctionManager.bid(10, "alice"), is("BID_ACCEPTED"));
  }

  @Test
  public void rejectsBidIfNotHighest() {
    AuctionManager auctionManager = new AuctionManager(paymentSystem, dispatcher);

    auctionManager.startAuction("clock", "bob");

    context.checking(new Expectations() {{
      exactly(0).of(paymentSystem).charge(5, "carole");
      exactly(1).of(paymentSystem).charge(10, "alice");
    }});

    auctionManager.bid(10, "alice");
    assertThat(auctionManager.bid(5, "carole"), is("BID_TOO_LOW"));
  }
  @Test
  public void acceptsBidIfHigherThanHighest() {
    AuctionManager auctionManager = new AuctionManager(paymentSystem, dispatcher);

    auctionManager.startAuction("clock", "bob");

    context.checking(new Expectations() {{
      exactly(0).of(paymentSystem).charge(5, "carole");
      exactly(1).of(paymentSystem).charge(10, "alice");
      exactly(1).of(paymentSystem).charge(20, "david");
    }});

    auctionManager.bid(10, "alice");
    auctionManager.bid(5, "carole");
    assertThat(auctionManager.bid(20, "david"), is("BID_ACCEPTED"));
  }

  @Test
  public void endsAuctionAndRefundsBidsAndDispatchesItem() {
    AuctionManager auctionManager = new AuctionManager(paymentSystem, dispatcher);

    auctionManager.startAuction("clock", "bob");

    context.checking(new Expectations() {{
      exactly(0).of(paymentSystem).charge(5, "carole");
      exactly(1).of(paymentSystem).charge(10, "alice");
      exactly(1).of(paymentSystem).charge(20, "david");
      exactly(1).of(paymentSystem).pay(10, "alice");
      exactly(1).of(paymentSystem).pay(20, "bob");
      exactly(1).of(dispatcher).dispatch("clock", "david");
    }});

    auctionManager.bid(10, "alice");
    auctionManager.bid(5, "carole");
    auctionManager.bid(20, "david");
    auctionManager.endAuction();
  }
}
