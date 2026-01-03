package arch.attanake.controller;

import arch.attanake.dto.ProductRequestDto;
import arch.attanake.dto.ProductResponseDto;
import arch.attanake.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Page<ProductResponseDto> getAllProducts(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "category") String category,
            @RequestParam(required = false, name = "minPrice") BigDecimal minPrice,
            @RequestParam(required = false, name = "maxPrice") BigDecimal maxPrice,
            @PageableDefault Pageable pageable) {
        return productService.searchProducts(name, category, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductResponseDto createProduct(@Valid @RequestBody ProductRequestDto request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequestDto request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @PatchMapping("/{id}/publish")
    public void publishProduct(@PathVariable String id, @RequestParam boolean published) {
        productService.setProductPublished(id, published);
    }
}
