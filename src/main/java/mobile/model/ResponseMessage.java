package mobile.model;

import lombok.*;

/**
 * Data Transfer Object representing a response message returned by the API.
 * Supports both string and object message types.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class ResponseMessage {

   private Object message; // changed from String to Object

   public boolean containsMessage(String expected) {
      return expected != null && message != null && message.toString().contains(expected);
   }
}