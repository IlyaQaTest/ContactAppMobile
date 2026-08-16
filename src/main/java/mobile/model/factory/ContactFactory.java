package mobile.model.factory;


import mobile.model.Contact;
import net.datafaker.Faker;

/**
 * Factory class for creating Contact objects with randomized test data.
 * Used to generate positive contact instances for API and UI tests.
 */
public class ContactFactory {

    private static final Faker faker = new Faker();

    /**
     * Creates a valid contact with random data.
     *
     * @return a Contact object populated with realistic test data
     */
    public static Contact positiveContact() {
        return Contact.builder()
                .name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .phone(faker.number().digits(13))
                .email(faker.internet().emailAddress())
                .address(faker.address().fullAddress())
                .description("My work")
                .build();
    }
}