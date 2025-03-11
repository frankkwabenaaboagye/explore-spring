package frank.product.services;

import frank.product.model.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    Product toProduct(ProductRequest productRequest){
        return Product.builder()
                .name(productRequest.name())
                .id(productRequest.id())
                .description(productRequest.description())
                .price(productRequest.price())
                .category(
                        Category.builder()
                                .id(productRequest.id())
                                .build()
                )
                .build();
    }

    ProductResponse toProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAvailableQuantity(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
        );
    }

    public ProductPurchaseResponse toProductPurchaseResponse(
            Product product,
            Double quantity)
    {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}
