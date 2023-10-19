package br.com.moisesconte.springbootmysql.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moisesconte.springbootmysql.domain.products.models.ProductModel;

public interface IProductRepository extends JpaRepository<ProductModel, UUID> {

}
