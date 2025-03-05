
package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {

 
    
     @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
      private int id;
     
     @Column(name = "userId",length = 45,nullable = false )
    private String userId;
     
      @Column(name = "lessonId",length = 45,nullable = false )
    private String lessonId;
      
      @Column(name = "lessonName",length = 45,nullable = false )
    private String lessonName;
      
      @Column(name = "lessonPrice",length = 10,nullable = false )
    private int lessonPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }
    
       public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getLessonPrice() {
        return lessonPrice;
    }

    public void setLessonPrice(int lessonPrice) {
        this.lessonPrice = lessonPrice;
    }

    public Cart() {
    }
    
    
    
    
}
