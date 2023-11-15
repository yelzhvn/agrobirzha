package kz.agrobirzha.app.Classes;

public class Product {
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private int price;
    private String image;
    private String unique_id;
    private int category_id;

    public Product(int id, String unique_id, String title, String shortdesc, int price, String image) {
        this.id = id;
        this.unique_id = unique_id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.price = price;
        this.image = image;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public int getPrice() {
        return price;
    }
    public String getImage() {
        return image;
    }
    public String getCurr() {
        return " тг.";
    }
    public String getUnique_id() {
        return unique_id;
    }
}