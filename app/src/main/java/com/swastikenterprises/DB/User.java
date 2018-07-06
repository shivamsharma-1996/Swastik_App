package com.swastikenterprises.DB;

public class User
{

    private String name;
    private String email;
    //private String photo;

    public User()
    {

    }

    /*public  static  User getInstance()
    {
        return new User();
    }
*/
    public User(String name, String email/* , String photo*/)
    {
        this.name = name;
        this.email = email;
        //this.photo = photo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    /*public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }*/

    @Override
    public String toString() {
        return name + " " + email + " "  /*photo*/;
    }
}
