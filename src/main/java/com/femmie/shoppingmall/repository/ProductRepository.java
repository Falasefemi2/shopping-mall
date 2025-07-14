package com.femmie.shoppingmall.repository;

import com.femmie.shoppingmall.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
