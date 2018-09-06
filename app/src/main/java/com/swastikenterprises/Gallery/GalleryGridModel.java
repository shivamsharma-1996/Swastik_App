package com.swastikenterprises.Gallery;


public class GalleryGridModel
{
    public String url;
//    private String title;
//    private String type;


    public GalleryGridModel()
    {
    }

    public GalleryGridModel(String img) {
        this.url = img;

    }

    public String getImg() 
    {
        return url;
    }

    public void setImg(String img) {
        this.url = img;
    }


   /* @Override
    public String toString()
    {
        return img + "" + title + "" + type;
    }*/
}
