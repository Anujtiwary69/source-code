package co.stutzen.shopzen.bo;

public class ProductBO {

    private int id;

    private String name;

    private String image;

    private int ratingCount;

    private int reviewUserCount;

    private double amount;

    private boolean isAddedBag;

    private double discountPercentage;

    private int quantity;

    private String type;

    private int variationId;

    private String size;

    private String color;

    public ProductBO(int id, String name, String image, int ratingCount, int reviewUserCount, double amount, boolean isAddedBag, double discountPercentage, String type){
        this.id = id;
        this.name = name;
        this.image = image;
        this.ratingCount = ratingCount;
        this.reviewUserCount = reviewUserCount;
        this.amount = amount;
        this.isAddedBag = isAddedBag;
        this.discountPercentage = discountPercentage;
        this.type  = type;
    }

    public ProductBO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public int getReviewUserCount() {
        return reviewUserCount;
    }

    public void setReviewUserCount(int reviewUserCount) {
        this.reviewUserCount = reviewUserCount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isAddedBag() {
        return isAddedBag;
    }

    public void setIsAddedBag(boolean isAddedBag) {
        this.isAddedBag = isAddedBag;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getVariationId() {
        return variationId;
    }

    public void setVariationId(int variationId) {
        this.variationId = variationId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
