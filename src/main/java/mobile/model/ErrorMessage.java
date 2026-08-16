package mobile.model;

import lombok.*;

import java.util.Map;

/**
 * Data Transfer Object representing an error message returned by the API.
 * Contains detailed information about the error, including timestamp, status, and message.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private String timestamp;
    private int status;
    private String error;
    private Map<String, String> message; // changed back to Map to match API response
    private String path;
}