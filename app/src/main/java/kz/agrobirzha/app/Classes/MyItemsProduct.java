package kz.agrobirzha.app.Classes;

public class MyItemsProduct {
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private int price;
    private String image;
    private String uid;

    public MyItemsProduct(int id, String title, String shortdesc, int price, String image) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.price = price;
        this.image = image;
        this.uid = uid;
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
    public String getUid() {
        return uid;
    }
}