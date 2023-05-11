package mss.pfe.fileIntegrator;

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
            "    public Processor(%s %s) {\n" +
                    "        this.%s = %s;\n" +
                    "    }\n";
    public static String saveObjectLine1="%s %s = new %s() ;\n";
    public static String saveObjectLine2="%s.set%s(\\\"%s\"\\\");\n";
    public static String saveObjectLine3="%s.save(%s);\n";

    // final customerRepo customerRepo;
    public static String repoDeclaration =
            "final %s %s ;\n";
    /*
    for(int i=0;i<file.length;i++){
        line1
        line2
        line3
        --------------------------------
        Customer customeri = new Customer();
        customeri.setName("bilel");
        customeri.setId("1");
        .......
        .
        .....
        ...
        customer1.setLocal("1");

        customerRepo.save(customer)
    }
     */
    public static String saveObjectCode=
            "for(int i=0;i<%s;i++){\n" +
                    "   %s" +
                    "}\n" ;
    public static String processFileSourceCode="" +
            packageCode+
            importCode+
            annotation+
            "public class Processor(){\n" +
            //processorConstructor
            "%s"+
            "@Autowired\n" +
            //repoDeclaration
            "%s"+
            "@Bean\n" +
            "public void processFile(){\n" +
            //saveObjectCode
            "%s" +
            "}\n" +
            "}";

}
