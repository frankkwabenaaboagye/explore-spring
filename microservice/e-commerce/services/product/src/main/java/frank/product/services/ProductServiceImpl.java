package frank.product.services;

import frank.product.exceptions.ProductPurchaseException;
import frank.product.interfaces.ProductService;
import frank.product.model.*;
import frank.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    @Override
    public List<ProductPurchaseResponse> purchaseProduct(List<ProductPurchaseRequest> productPurchaseRequests) {

        List<Integer> productIds = productPurchaseRequests
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        List<Product> storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseException("One or more products does not exists");
        }

        var storesRequest = productPurchaseRequests
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        List<ProductPurchaseResponse> purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for(int i=0; i<storedProducts.size(); i++){

            Product product = storedProducts.get(i);
            ProductPurchaseRequest request = storesRequest.get(i);

            if(product.getAvailableQuantity() < request.quantity()){
                throw new ProductPurchaseException("Product with id: "+product.getId()+" has insufficient quantity");
            }

            product.setAvailableQuantity(product.getAvailableQuantity() - request.quantity());
            productRepository.save(product);

            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, request.quantity()));
        }


        return purchasedProducts;
    }

    @Override
    public ProductResponse findProductByTheId(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
