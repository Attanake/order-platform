package arch.attanake.service.impl;

import arch.attanake.dto.ProductRequest;
import arch.attanake.dto.ProductResponse;
import arch.attanake.entity.ProductEntity;
import arch.attanake.exception.ProductNotFoundException;
import arch.attanake.mapper.ProductMapper;
import arch.attanake.repository.ProductRepository;
import arch.attanake.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> searchProducts(
            String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        return productRepository.searchProducts(name, category, minPrice, maxPrice, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse getProductById(String id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        ProductEntity product = productMapper.toEntity(request);
        product.setPublished(false);
        ProductEntity savedProduct = productRepository.save(product);
        log.info("Saved product {}", savedProduct);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(String id, ProductRequest request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productMapper.updateFromRequest(request, product);
        ProductEntity updatedProduct = productRepository.save(product);
        log.info("Product updated: {}", product);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        log.info("Product deleted: {}", id);
    }

    @Override
    @Transactional
    public void setProductPublished(String id, boolean published) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setPublished(published);
        productRepository.save(product);

        log.info("Product {} publication status changed to {}", id, published);
    }
}
