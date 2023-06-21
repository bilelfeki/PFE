package mss.pfe.fileIntegrator.entities;
import jakarta.persistence.*;

@Entity
@Table(schema ="porteur")
public class test{
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
public test(){
}
private String reference;
public void setreference(String reference){
this.reference=reference;
}
private String name;
public void setname(String name){
this.name=name;
}
}