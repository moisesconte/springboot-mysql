package br.com.moisesconte.springbootmysql.domain.products;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "products")
public class ProductModel {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;
  private String description;
  private double price;
}
