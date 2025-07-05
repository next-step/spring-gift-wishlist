package com.example.demo.entity;

public class Product {
  private Long id;
  private String name;
  private int price;
  private String imageUrl;

  public Product(Long id, String name, int price, String imageUrl){
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Product() {
  }

  public void update(String name, int price, String imageUrl) {
    if (price < 0) throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Product(String name, int price, String imageUrl) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
  }

  public Long getId(){
    return id;
  }

  public String getName(){
    return name;
  }

  public int getPrice(){
    return price;
  }

  public String getImageUrl(){
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setPrice(int price){
    this.price = price;
  }

  public void setName(String name){
    this.name = name;
  }

  public void setId(Long id){
    this.id = id;
  }
}


