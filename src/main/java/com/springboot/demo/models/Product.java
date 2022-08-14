package com.springboot.demo.models;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

//POJO
@Entity
@Table(name = "tblProduct")
public class Product {
    @Id
    @SequenceGenerator(
            name = "test",
            sequenceName = "test",
            allocationSize = 1 //Inclement by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "test"
    )
    private long id;
    @Column(nullable = false, unique = true)
    private String productName;
    private int manufactureDate;
    private int price;
    private String url;

    //caculated value = transient
    @Transient
    private int age;
    private int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - manufactureDate;
    }

    public Product(){}

    public Product(String productName, int year, int price, String url) {
        this.productName = productName;
        this.manufactureDate = year;
        this.price = price;
        this.url = url;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(int manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", year=" + manufactureDate +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && manufactureDate == product.manufactureDate && price == product.price && Objects.equals(productName, product.productName) && Objects.equals(url, product.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, manufactureDate, price, url);
    }
}
