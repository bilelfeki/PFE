package mss.pfe.fileIntegrator.entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.*;
@Configuration
public class Processor1{
final testRepo testRepo ;
    public Processor1(testRepo testRepo) {
        this.testRepo = testRepo;
    }
public void Processor1(){
}
@Bean
public void readFile(){
String filePath="C:/Users/bilel/Desktop/entityCreator/executable/src/main/java/mss//pfe/fileIntegrator/File";
File sourceFile1 = new File(filePath).listFiles()[0];
try {
FileReader fileReader = new FileReader(sourceFile1);
BufferedReader reader = new BufferedReader(fileReader);

String line;
while ((line = reader.readLine()) != null) {
System.out.println(line);
test test_ = new test() ;
test_.setreference(line.substring(1,4));
test_.setname(line.substring(5,19));
testRepo.save(test_);
}
} catch (FileNotFoundException e) {
throw new RuntimeException(e);
} catch (IOException e) {
throw new RuntimeException(e);
}


}
}