package giper;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by prochiy on 8/21/15.
 */
@Entity
@Table(name = "person")
public class User implements Serializable {

    public User(){}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String name;
    private String family;
    private Boolean status;
    private String imageURL;
    private Integer age;

    public int getIntImageURL(){
        return Integer.parseInt(imageURL.substring(imageURL.lastIndexOf("/")+1, imageURL.lastIndexOf(".")));
    }

    @Override
    public String toString(){
        return "id = " + id + "\n" +
                "name = " + name + "\n" +
                "family = " + family + "\n" +
                "status = " + status + "\n" +
                "age = " + age;
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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
