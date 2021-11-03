package dash.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's address in the address book.
 * Guarantees: immutable;
 */
public class Address {

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address An address.
     */
    public Address(String address) {
        requireNonNull(address);
        this.value = address;
    }

    public Address() {
        this.value = "";
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Address // instanceof handles nulls
                && value.equals(((Address) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
