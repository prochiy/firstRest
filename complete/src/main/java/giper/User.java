package giper;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by prochiy on 8/21/15.
 */
@Entity
@Table(name = "rest_user")
public class User implements Serializable {

    public User(){}

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String family;
    @Column(nullable = true, columnDefinition = "default '0")
    private Boolean status;
    @Column(nullable = true)
    private String imageURL;
    @Column(nullable = true)
    private Integer age;
    @Column(nullable = false, columnDefinition = "default '-1'")
    private long timestamp;

    @Override
    public String toString(){
        return "id = " + id + "\n" +
                "name = " + name + "\n" +
                "family = " + family + "\n" +
                "status = " + status + "\n" +
                "age = " + age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
