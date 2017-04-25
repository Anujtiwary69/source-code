package co.stutzen.shopzen.bo;

import com.google.android.gms.maps.model.Marker;

public class StoreBo {

    private int ShopImg;

    private double latitude;

    private double longitude;

    private Marker marker;

    private String shopaddress;

    public int getShopImg() {
        return ShopImg;
    }

    public void setShopImg(int shopImg) {
        ShopImg = shopImg;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public void setShopaddress(String shopaddress) {
        this.shopaddress = shopaddress;
    }
}
