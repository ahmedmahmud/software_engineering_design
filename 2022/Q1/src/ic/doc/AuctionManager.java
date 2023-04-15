package ic.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import static java.util.Map.entry;

public class AuctionManager {
    private int highestBid;
    private String seller;
    private String item;
    private final PaymentSystem paymentSystem;
    private final Dispatcher dispatcher;

    private final List<Entry<String, Integer>> bids;

    public AuctionManager(PaymentSystem paymentSystem, Dispatcher dispatcher) {
        this.paymentSystem = paymentSystem;
        this.dispatcher = dispatcher;
        bids = new ArrayList<>();
    }

    public void startAuction(String item, String seller) {
        this.highestBid = 0;
        this.seller = seller;
        this.item = item;
    }

    public String bid(int bid, String bidder) {
        if (bid > highestBid) {
            highestBid = bid;
            bids.add(entry(bidder, bid));
            paymentSystem.charge(bid, bidder);
            return "BID_ACCEPTED";
        } else {
            return "BID_TOO_LOW";
        }
    }

    public void endAuction() {
        Entry<String, Integer> last = bids.remove(bids.size() - 1);

        for (Entry<String, Integer> bid : bids) {
            paymentSystem.pay(bid.getValue(), bid.getKey());
        }

        paymentSystem.pay(last.getValue(), seller);
        dispatcher.dispatch(item, last.getKey());
    }
}
