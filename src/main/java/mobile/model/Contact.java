package mobile.model;

import lombok.*;

/**
 * Represents a contact entity in the PhoneBook application.
 * Contains basic contact details such as name, phone, and email.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private String id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String description;
}