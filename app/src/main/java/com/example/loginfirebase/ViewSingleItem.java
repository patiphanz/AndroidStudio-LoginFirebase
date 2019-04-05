package com.example.loginfirebase;

public class ViewSingleItem {

    private String Image_URL, Image_Title;

    public ViewSingleItem(String image_url, String image_title) {
        Image_URL = image_url;
        Image_Title = image_title;
    }

    public ViewSingleItem() {

    }

    public String getImage_url() {
        return Image_URL;
    }

    public void setImage_url(String image_url) {
        Image_URL = image_url;
    }

    public String getImage_title() {
        return Image_Title;
    }

    public void setImage_title(String image_title) {
        Image_Title = image_title;
    }
}
