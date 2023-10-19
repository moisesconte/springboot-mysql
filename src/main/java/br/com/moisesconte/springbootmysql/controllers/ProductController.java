package br.com.moisesconte.springbootmysql.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.moisesconte.springbootmysql.repositories.IProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  private IProductRepository productRepository;

  @GetMapping("/all")
  public ResponseEntity<?> products() {
    var products = this.productRepository.findAll();

    return ResponseEntity.ok().body(products);
  }
}
