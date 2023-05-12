package mss.pfe.fileIntegrator;

import java.util.ArrayList;

public class DefaultCode {
    public static String RepositorySourceCode=
            "package mss.pfe.fileIntegrator.entities;\n" +
            "import org.springframework.data.jpa.repository.JpaRepository;\n" +
            "public interface " + "%s"+"Repo extends JpaRepository<"+"%s"+", Long> {\n" +
            "}";
    public static  String EntitySourceCode =
            "package mss.pfe.fileIntegrator.entities;\n" +
                    "import jakarta.persistence.*;\n\n" +
                    "@Entity\n" +
                    "@Table(schema =\"%s\")\n" +
                    "public class " + "%s" + "{\n" +
                    "@Id\n" +
                    "@GeneratedValue(strategy = GenerationType.AUTO)\n" +
                    "private Long id;\n" +
                    "public "+"%s"+"(){\n" +
                    "}\n" +
                    "";
    private static String packageCode=
                    "package mss.pfe.fileIntegrator.entities;\n" ;
    private static String importCode=
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" ;
    private static String annotation =
            "@Configuration\n" ;
    public static String processorConstructor=
            //%s is the repoName
            "    public Processor%s(%s %s) {\n" +
                    "        this.%s = %s;\n" +
                    "    }\n";
    public static String saveObjectLine1="%s %s = new %s() ;\n";
    public static String saveObjectLine2="%s.set%s(\"%s\");\n";
    public static String saveObjectLine3="%s.save(%s);\n";

    // final customerRepo customerRepo;
    public static String repoDeclaration =
            "final %s %s ;\n";
    /*
        line1
        line2
        line3
        --------------------------------
        Customer customeri = new Customer();
        customeri.setName("bilel");
        customeri.setId("1");
        ...
        .
        ..
        ...
        customer1.setLocal("1");

        customerRepo.save(customer)
    }
     */
    public static String processFileSourceCode="" +
            packageCode+
            importCode+
            annotation+
            //fileLineIndex
            "public class Processor%s{\n" +
            //processorConstructor
            "%s"+
            //repoDeclaration
            "%s"+
            "@Bean\n" +
            "public void Processor%s(){\n" +
            //saveObjectCode
            "%s" +
            "}\n" +
            "}";

}
