package com.springboot.demo.controllers;

import com.springboot.demo.models.Product;
import com.springboot.demo.models.ResponseObject;
import com.springboot.demo.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/products")
public class ProductController {
    @Autowired
    private ProductRepo productRepo;
    /**
     *  Get all products
     * @return all products
     */
    @GetMapping("/")
    ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.status(HttpStatus.OK).body(productRepo.findAll());
    }

    /**
     *
     * @param id: input id
     * @return null or found product
     */
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findProductById(@PathVariable long id){

        Optional<Product> product= productRepo.findById(id);

        return product.isPresent()?
        ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Query product by id OK", product)
        ):
        ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("NG", "Cannot find product by id = " + id, "")
        );
    }

    /**
     * post method
     * @param product input product
     * @return input product
     */
    @PostMapping(value = "/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product product){
        List<Product> listProduct = productRepo.findProductByProductName(product.getProductName());
        if (listProduct.size() > 0) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new ResponseObject("fail", "Product name already exist", productRepo.save(product))
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Product successfully", productRepo.save(product))
        );
    }

    /**
     *
     * @param product
     * @param id
     * @return
     */
    @PutMapping(value = "/update/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product product, @PathVariable long id){
        Product newProduct = productRepo.findById(id).map(pro -> {
            pro.setProductName(product.getProductName());
            pro.setUrl(product.getUrl());
            pro.setPrice(product.getPrice());
            pro.setManufactureDate(product.getManufactureDate());
            productRepo.save(pro);
            return pro;
        }).orElseGet(()->{
            product.setId(id);
            productRepo.save(product);
            return product;
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Update product successfully", newProduct)
        );
    }

    /**
     * Delete by id
     * @param id pk
     * @return  id
     */
    @DeleteMapping(value = "/delete/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable long id){
        boolean exist = productRepo.existsById(id);
        if(exist){
            productRepo.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Delete successfully", id));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("No product id found", "Delete failed", id));
    }
}
