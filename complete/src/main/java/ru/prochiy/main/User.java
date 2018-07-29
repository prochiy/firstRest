package ru.prochiy.main;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static com.sun.javafx.fxml.expression.Expression.not;

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
    @Column(nullable = true, columnDefinition = "boolean default true")
    private Boolean status;
    @Column(nullable = true)
    private String imageURL;
    @Column(nullable = true)
    private Integer age;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Override
    public String toString(){
        return "id = " + id + "\n" +
                "name = " + name + "\n" +
                "family = " + family + "\n" +
                "status = " + status + "\n" +
                "age = " + age;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        createdAt = new Date();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof User)){
            return false;
        }
        User u = (User)o;
        return this.name.equals(u.name);
    }

    @Override
    public int hashCode(){
        return name.hashCode();
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getStatus() {
        return status;
    }
}
