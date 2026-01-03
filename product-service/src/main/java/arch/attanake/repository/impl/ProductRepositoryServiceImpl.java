package arch.attanake.repository.impl;

import arch.attanake.entity.ProductEntity;
import arch.attanake.repository.ProductRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryServiceImpl implements ProductRepositoryService {
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ProductEntity> search(
            String name,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    ) {
        List<Criteria> criteria = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteria.add(Criteria.where("name").regex(name, "i"));
        }

        if (category != null) {
            criteria.add(Criteria.where("category").is(category));
        }

        if (minPrice != null && maxPrice != null) {
            criteria.add(Criteria.where("price").gte(minPrice).lte(maxPrice));
        }

        Query query = new Query();
        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }

        query.with(pageable);

        List<ProductEntity> products = mongoTemplate.find(query, ProductEntity.class);
        long count = mongoTemplate.count(query.skip(0).limit(0), ProductEntity.class);

        return new PageImpl<>(products, pageable, count);
    }

}
