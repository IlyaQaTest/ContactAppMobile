package mobile.model.factory;

import mobile.model.User;
import net.datafaker.Faker;

/**
 * Factory class for creating User objects with randomized test data.
 * Used to generate valid user instances for registration and login tests.
 */
public class UserFactory {

    private static final Faker faker = new Faker();

    /**
     * Creates a valid user with random email and predefined password.
     *
     * @return a User object populated with realistic test data
     */
    public static User positiveUser() {
        return new User(faker.internet().emailAddress(), "Password123!");
    }
}