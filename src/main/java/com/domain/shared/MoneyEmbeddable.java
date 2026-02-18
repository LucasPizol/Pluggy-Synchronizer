package com.domain.shared;

import java.math.BigDecimal;
import javax.money.MonetaryAmount;
import org.javamoney.moneta.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MoneyEmbeddable {

  @Column(name = "value_subcents")
  private Long valueCents;

  @Column(name = "value_currency")
  private String currency;

  protected MoneyEmbeddable() {
  }

  public MoneyEmbeddable(Long valueCents, String currency) {
    this.valueCents = valueCents;
    this.currency = currency;
  }

  public MonetaryAmount toMonetaryAmount() {
    String currency = this.currency;
    if (currency.equals("br")) {
      currency = "BRL";
    }
    return Money.of(
        BigDecimal.valueOf(valueCents, 2),
        currency);
  }

  public static MoneyEmbeddable from(MonetaryAmount amount) {
    return new MoneyEmbeddable(
        amount.getNumber().numberValueExact(BigDecimal.class)
            .movePointRight(2)
            .longValueExact(),
        amount.getCurrency().getCurrencyCode());
  }

  public Long getValueCents() {
    return valueCents;
  }

  public String getCurrency() {
    return currency;
  }

  public void setValueCents(Long valueCents) {
    this.valueCents = valueCents;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String toString() {
    return toMonetaryAmount().toString();
  }
}
