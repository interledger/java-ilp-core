package org.interledger;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Interledger Protocol (ILP) Addresses identify Ledger accounts (or groups of Ledger accounts) in
 * an ILP network, and provide a way to route a payment to its intended destination.
 *
 * <p>Interledger Addresses can be subdivided into two categories:</p>
 *
 * <p> <b>Destination Addresses</b> are complete addresses that can receive payments. A destination
 * address always maps to one account in a ledger, though it can also provide more specific
 * information, such as an invoice ID or a sub-account. Destination addresses MUST NOT end in a
 * period (.) character. </p>
 *
 * <p> <b>Address Prefixes</b> are incomplete addresses representing a grouping of destination
 * addresses. Many depths of grouping are possible, for example: groups of accounts or sub-accounts;
 * an individual ledger or sub-ledger; or entire neighborhoods of ledgers. Address prefixes MUST end
 * in a period (.) character. </p>
 *
 * <p> The formal specification for an Interledger Addresses is defined in <a
 * href="https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses">Interledger RFC
 * #15</a>. </p>
 *
 * @see "https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses"
 */
public interface InterledgerAddress {

  /**
   * Constructor to allow quick create of String.
   *
   * @param value String representation of an Interledger Address
   * @return a {@link InterledgerAddress} instance.
   */
  static InterledgerAddress of(final String value) {
    return new Builder()
        .value(value)
        .build();
  }

  /**
   * Get the default builder.
   *
   * @return a {@link Builder} instance.
   */
  static Builder builder() {
    return new Builder();
  }

  /**
   * Return this address's value as a non-null {@link String}. For example:
   * <code>us.usd.bank.account</code>
   *
   * @return A {@link String} representation of this Interledger address.
   */
  String getValue();

  /**
   * Tests if this Interledger address represents a ledger prefix.
   *
   * @return True if the address is a ledger prefix, false otherwise.
   */
  default boolean isLedgerPrefix() {
    return getValue().endsWith(".");
  }

  /**
   * Tests if this InterledgerAddress starts with the specified {@code addressSegment}.
   *
   * @param addressSegment An {@link String} prefix to compare against.
   *
   * @return {@code true} if this InterledgerAddress begins with the specified prefix.
   */
  default boolean startsWith(final String addressSegment) {
    Objects.requireNonNull(addressSegment, "addressSegment must not be null!");
    return this.getValue()
        .startsWith(addressSegment);
  }

  /**
   * Tests if this InterledgerAddress starts with the specified {@code interledgerAddress}.
   *
   * @param interledgerAddress An {@link InterledgerAddress} prefix to compare against.
   *
   * @return {@code true} if this InterledgerAddress begins with the specified prefix.
   */
  default boolean startsWith(final InterledgerAddress interledgerAddress) {
    Objects.requireNonNull(interledgerAddress, "interledgerAddress must not be null!");
    return this.startsWith(interledgerAddress.getValue());
  }

  /**
   * <p>Return a new InterledgerAddress by postfixing the supplied {@code segment} to this address.
   * </p>
   *
   * <p>This method can be used to construct both address prefixes and destination addresses. For
   * example, if the value of this address is '<code>us.usd.</code>', then calling this method with
   * an argument of '<code>bob</code>' would result in a new Interledger Address with a value of
   * '<code>us.usd.bob</code>', which is a destination address.</p>
   *
   * <p>Likewise, if the value of this address is '<code>us.usd.pacific.</code>', then calling this
   * method with an argument of '<code>creditunions.</code>' would result in a new Interledger
   * Address with a value of '<code>us.usd.pacific.creditunions.</code>', which is an address
   * prefix.</p>
   *
   * @param addressSegment A {@link String} to be appended to this address as an additional
   *                       segment.
   *
   * @return A new instance representing the original address with a newly specified final segment.
   */
  default InterledgerAddress with(String addressSegment) {
    Objects.requireNonNull(addressSegment, "addressSegment must not be null!");

    final StringBuilder sb = new StringBuilder(this.getValue());
    if (!this.isLedgerPrefix()) {
      sb.append(".");
    }
    sb.append(addressSegment);

    return builder().value(sb.toString()).build();
  }

  /**
   * <p>Return this address's prefix, which is a new {@link InterledgerAddress} containing the
   * characters inside of {@link #getValue()}, up-to and including the last period. If this address
   * is already a prefix, then this instance is instead returned unchanged.</p>
   *
   * <p>For example, calling this method on an address 'g.example.alice' would yield a new address
   * containing 'g.example.'. Conversely, calling this method on an address that is already a
   * prefix, like 'g.example.' would yield the same instance, 'g.example.'.</p>
   *
   * @return A potentially new {@link InterledgerAddress} representing the prefix of this address.
   */
  default InterledgerAddress getPrefix() {
    if (this.isLedgerPrefix()) {
      return this;
    } else {
      return InterledgerAddress.of(getValue().substring(0, this.getValue().lastIndexOf(".") + 1));
    }
  }

  /**
   * <p>Compares the specified object with this <tt>InterledgerAddress</tt> for equality. The
   * <tt>InterledgerAddress</tt> interface is essentially a type-safe wrapper around a String value,
   * so implementations should take care to forward equality decisions to the {@link
   * String#equals(Object)} method on the object returned of {@link #getValue()}.</p>
   *
   * @param obj object to be compared for equality with this collection
   *
   * @return <tt>true</tt> if the specified object is equal to this collection
   *
   * @see Object#equals(Object)
   * @see Set#equals(Object)
   * @see List#equals(Object)
   */
  @Override
  boolean equals(Object obj);

  /**
   * <p> Returns the hash code value for this <tt>InterledgerAddress</tt>. The
   * <tt>InterledgerAddress</tt> interface is essentially a type-safe wrapper around a String value,
   * so implementations should take care to forward hashcode decisions to the {@link
   * String#equals(Object)} method on the object returned of {@link #getValue()}.</p>
   *
   * @return the hash code value for this InterledgerAddress.
   *
   * @see Object#hashCode()
   * @see Object#equals(Object)
   */
  @Override
  int hashCode();

  /**
   * A builder for immutable instances of {@link InterledgerAddress}.
   *
   * <p> <em>NOTE: {@code Builder} is not thread-safe and generally should not be stored in a field
   * or collection, but instead used immediately to create instances.</em> </p>
   */
  class Builder {

    private String value;

    /**
     * No-args Constructor.
     */
    private Builder() {
    }

    /**
     * Builder method to actually construct an instance of {@link InterledgerAddress} of the data in
     * this builder.
     * @return An {@link InterledgerAddress} instance
     */
    public InterledgerAddress build() {
      return new Impl(this);
    }

    /**
     * Assign a new value to this builder.
     *
     * @param value A {@link String} representing this builder's "value", which is the string
     *              version of an Interledger Address.
     * @return This {@link Builder} instance.
     */
    public Builder value(final String value) {
      this.value = value;
      return this;
    }

    /**
     * A private, immutable implementation of {@link InterledgerAddress}. To construct an instance
     * of this class, use an instance of {@link Builder}.
     */
    private static final class Impl implements InterledgerAddress {

      private static final String REGEX = "(?=^.{1,1023}$)"
          + "^(g|private|example|peer|self|test[1-3])[.]([a-zA-Z0-9_~-]+[.])*([a-zA-Z0-9_~-]+)?$";

      private static final Pattern PATTERN = Pattern.compile(REGEX);

      private final String value;

      /**
       * Required-args Constructor.
       *
       * @param builder An non-null instance of {@link Builder} to construct a new instance of.
       */
      private Impl(final Builder builder) {
        Objects.requireNonNull(builder, "Builder must not be null!");
        Objects.requireNonNull(builder.value, "value must not be null!");

        if (!isValidInterledgerAddress(builder.value)) {
          throw new IllegalArgumentException(
              String.format(
                  "Invalid characters in address: ['%s']. "
                      + "Reference Interledger RFC-15 for proper format.",
                  builder.value)
          );
        }

        this.value = builder.value;
      }

      /**
       * Helper method to determine if an Interledger Address conforms to the specifications
       * outlined in Interledger RFC #15.
       *
       * @param value A {@link String} representing a potential Interledger Address value.
       *
       * @return {@code true} if the supplied {@code value} conforms to the requirements of RFC 15;
       *     {@code false} otherwise.
       *
       * @see "https://github.com/interledger/rfcs/tree/master/0015-ilp-addresses"
       */
      private boolean isValidInterledgerAddress(final String value) {
        Objects.requireNonNull(value);
        return PATTERN.matcher(value)
            .matches();
      }

      /**
       * Accessor method for this address's {@link String} value. <p> NOTE: This is distinct of
       * {@link #toString()} to allow for the two values to diverge, e.g., for debugging or logging
       * purposes. </p>
       *
       * @return The value of the {@code value} attribute
       */
      @Override
      public String getValue() {
        return this.value;
      }

      @Override
      public boolean equals(final Object object) {
        if (this == object) {
          return true;
        }
        if (object == null || getClass() != object.getClass()) {
          return false;
        }

        Impl interledgerAddressImpl = (Impl) object;

        return value.equals(interledgerAddressImpl.value);
      }

      @Override
      public int hashCode() {
        return value.hashCode();
      }

      @Override
      public String toString() {
        return this.value;
      }
    }
  }

}
