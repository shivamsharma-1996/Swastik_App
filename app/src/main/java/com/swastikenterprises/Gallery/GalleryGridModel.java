package com.swastikenterprises.Gallery;


public class GalleryGridModel
{
    public String img;
//    private String title;
//    private String type;


    public GalleryGridModel()
    {
    }

    public GalleryGridModel(String img, String title, String type) {
        this.img = img;
//        this.title = title;
//        this.type = type;
    }

    public String getImg() 
    {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

  /*  public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return img + "" + title + "" + type;
    }*/
}
