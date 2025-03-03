package frank.customer.models;


public record CustomerResponse(
        String id,
        String name,
        String email,
        Address address
) {

}
