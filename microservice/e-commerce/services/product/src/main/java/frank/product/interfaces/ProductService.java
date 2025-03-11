package frank.product.interfaces;

import frank.product.model.ProductPurchaseRequest;
import frank.product.model.ProductPurchaseResponse;
import frank.product.model.ProductRequest;
import frank.product.model.ProductResponse;

import java.util.List;

public  interface ProductService {

    Integer createProduct(ProductRequest productRequest);
    List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> productPurchaseRequests);
    ProductResponse findProductByTheId(Integer productId);
    List<ProductResponse> getAllProducts();

}
