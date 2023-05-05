package mss.pfe.fileIntegrator;

public class DefaultCode {
    public static String RepositorySourceCode="package mss.pfe.fileIntegrator.entities;\n" +
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
    public static String processFileSourceCode="" +
            "package mss.pfe.fileIntegrator.entities;\n" +
            "@Configuration\n" +
            "public class FileProcessor(){\n" +
            "@Bean\n" +
            "public void processFile(){\n" +
            "%s" +
            "}\n" +
            "}";

}
