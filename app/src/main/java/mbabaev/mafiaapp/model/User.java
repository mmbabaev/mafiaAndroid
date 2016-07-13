package mbabaev.mafiaapp.model;

/**
 * Created by Sammy on 14.07.16.
 */
public class User {

    public User(String name, boolean is_alive, String role, String image_url, int id) {
        this.name = name;
        this.is_alive = is_alive;
        this.role = role;
        this.image_url = image_url;
        this.id = id;
    }

    public String name;
    public boolean is_alive;
    public String image_url;
    public String role;
    public int id;
}
