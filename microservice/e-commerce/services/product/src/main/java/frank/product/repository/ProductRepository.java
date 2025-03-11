package frank.product.repository;

import frank.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // two part "findAllByIdIn" (match any value inside the given list ) "OrderById" (ascending order)
    List<Product> findAllByIdInOrderById(List<Integer> productIds);
}
