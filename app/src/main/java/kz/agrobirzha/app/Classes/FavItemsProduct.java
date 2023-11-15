package kz.agrobirzha.app.Classes;

public class FavItemsProduct {
    private int id;
    private String title;
    private String shortdesc;
    private int price;
    private String image;
    private String unique_id;

    public FavItemsProduct() {

    }
    public FavItemsProduct(int id, String unique_id, String title, String shortdesc, int price, String image) {
        this.id = id;
        this.unique_id = unique_id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.price = price;
        this.image = image;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
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

}