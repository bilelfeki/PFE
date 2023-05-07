package mss.pfe.fileIntegrator.entities;
import jakarta.persistence.*;

@Entity
@Table(schema ="GHY")
public class custmer{
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
public custmer(){
}
private String name;
public void setname(String name){
this.name=name;
}
private String c2;
public void setc2(String c2){
this.c2=c2;
}
}