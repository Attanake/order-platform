package arch.attanake.repository;

import arch.attanake.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {
    boolean existsByIdAndPublishedTrue(String productId);

    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<ProductEntity> findByCategory(String category, Pageable pageable);

    Page<ProductEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("{ 'id': ?0 }")
    Optional<ProductEntity> findByCustomId(String id);

    @Query(value = "{}", fields = "{'id' : 1}")
    List<ProductEntity> findAllIds();

    @Query("{ $and: [ " +
            "  { 'name': { $regex: ?0, $options: 'i' } }, " +
            "  { 'category': ?1 }, " +
            "  { 'price': { $gte: ?2, $lte: ?3 } } " +
            "] }")
    Page<ProductEntity> searchProducts(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}