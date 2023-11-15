package kz.agrobirzha.app.Classes;

public class CartItemsProduct {
    private int id;
    private String unique_id;
    private String title;
    private int number;
    private int price;
    private String image;

    public CartItemsProduct() {

    }
    public CartItemsProduct(int id, String unique_id, String title, int number, int price, String image) {
        this.id = id;
        this.unique_id = unique_id;
        this.title = title;
        this.number = number;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
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

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber(){
        return number;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
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


}