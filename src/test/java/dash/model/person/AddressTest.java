package dash.model.person;

import org.junit.jupiter.api.Test;

import dash.testutil.Assert;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Address(null));
    }
}
