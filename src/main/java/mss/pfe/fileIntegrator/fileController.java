package mss.pfe.fileIntegrator;

import jakarta.persistence.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

@RestController
@Component

public class fileController {

    @PersistenceContext
    private EntityManager entityManager;
    @CrossOrigin("*")
    @PostMapping("/api/v1/file_integrator")
    public void processFile(@RequestParam("File") MultipartFile file,
                            @RequestParam("entitiesString") String entitiesString,
                            @RequestParam("configString") String configString){


        String className = "okl";
        String entityName="okl";
        String EntitySourceCode =
                "package mss.pfe.fileIntegrator;\n;\n" +
                        "import jakarta.persistence.*;\n\n" +
                        "@Entity\n" +
                        "public class " + className + "{\n" +
                        "@Id\n" +
                        "@GeneratedValue(strategy = GenerationType.AUTO)\n" +
                        "private Long id;\n" +
                        "public "+entityName+"(){\n" +
                        "}\n" +
                        "}";
        String RepositorySourceCode="package mss.pfe.fileIntegrator;\n;\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "public interface " + className+"Repo extends JpaRepository<"+className+", Long> {\n" +
                "}";
        System.out.println(RepositorySourceCode);
        System.out.println(EntitySourceCode);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File sourceFile1 = new File(className + ".java");
        File sourceFile2 = new File(className + "Repo" +".java");
        Writer writer1 = null;
        Writer writer2=null;
        try {
            String filePath="C:\\Users\\bilel\\Desktop\\entityCreator\\fileIntegrator\\src\\main\\java\\mss\\pfe\\fileIntegrator/";
            writer1 = new FileWriter(filePath+sourceFile1);
            writer2= new FileWriter(filePath+sourceFile2);
            writer1.write(EntitySourceCode);
            writer2.write(RepositorySourceCode);
            writer1.close();
            writer2.close();


            //for compilation
            StandardJavaFileManager fileManager2 = compiler.getStandardFileManager(null, null, null);
            File file2 = new File("C:\\Users\\bilel\\Desktop\\entityCreator\\fileIntegrator\\src\\main\\java\\mss\\pfe\\fileIntegrator/"+className+".java");
            JavaFileObject javaFileObject = fileManager.getJavaFileObjects(file2).iterator().next();
            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(javaFileObject);
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager2, null, Arrays.asList("-d", "target/classes/"), null, compilationUnits);
            boolean success = task.call();

            //create the instance
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> myClass = classLoader.loadClass("mss.pfe.fileIntegrator.okl"); // Replace with the fully qualified name of your class
            Object entity = myClass.newInstance();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }  catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    }
