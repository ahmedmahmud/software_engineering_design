package ic.doc;

public interface PaymentSystem {
    void charge(int amount, String account);

    void pay(int amount, String account);
}
