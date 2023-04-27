package mss.pfe.fileIntegrator;

public class DefaultCode {
    public static String RepositorySourceCode="package mss.pfe.fileIntegrator;\n;\n" +
            "import org.springframework.data.jpa.repository.JpaRepository;\n" +
            "public interface " + "%s"+"Repo extends JpaRepository<"+"%s"+", Long> {\n" +
            "}";
    public static  String EntitySourceCode =
            "package mss.pfe.fileIntegrator\n;\n" +
                    "import jakarta.persistence.*;\n\n" +
                    "@Entity\n" +
                    "public class " + "%s" + "{\n" +
                    "@Id\n" +
                    "@GeneratedValue(strategy = GenerationType.AUTO)\n" +
                    "private Long id;\n" +
                    "public "+"%s"+"(){\n" +
                    "}\n" +
                    "";
}
