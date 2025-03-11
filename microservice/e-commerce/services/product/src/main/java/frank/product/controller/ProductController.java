package frank.product.controller;

import frank.product.interfaces.ProductService;
import frank.product.model.ProductPurchaseRequest;
import frank.product.model.ProductPurchaseResponse;
import frank.product.model.ProductRequest;
import frank.product.model.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Integer> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        return ResponseEntity.ok(productService.createProduct(productRequest));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProduct(@RequestBody @Valid List<ProductPurchaseRequest> productPurchaseRequests) {

        return ResponseEntity.ok(productService.purchaseProduct(productPurchaseRequests));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.findProductByTheId(productId));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

}
