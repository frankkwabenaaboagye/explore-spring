package frank.customer.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public record CustomerRequest(
        String id,

        @NotNull(message = "customer full name is required")
        String name,

        @Email(message = "email should be a valid email address")
        @NotNull(message = "email is required")
        String email,

        Address address
) {

}
