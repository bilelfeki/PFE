package mss.pfe.fileIntegrator.entities;
import jakarta.persistence.*;

@Entity
@Table(schema ="ghy")
public class Customer{
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Long id;
public Customer(){
}
private String champ;
public void setchamp(String champ){
this.champ=champ;
}
private Integer champ2;
public void setchamp2(Integer champ2){
this.champ2=champ2;
}
private  String champ5;
public void setchamp5( String champ5){
this.champ5=champ5;
}
private Integer champ8;
public void setchamp8(Integer champ8){
this.champ8=champ8;
}
}