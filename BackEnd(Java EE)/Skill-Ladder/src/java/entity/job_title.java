
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "job_title")
public class job_title implements Serializable{
    
       @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status_id;

    @ManyToOne
    @JoinColumn(name = "job_field_id")
    private job_field job_field;

    @Column(name = "name", length = 45, nullable = false)
    private String name;

    public job_title() {
    }

    public job_title(int id, Status status_id, job_field job_field, String name) {
        this.id = id;
        this.status_id = status_id;
        this.job_field = job_field;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Status status_id) {
        this.status_id = status_id;
    }

    public job_field getJob_field() {
        return job_field;
    }

    public void setJob_field(job_field job_field) {
        this.job_field = job_field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
