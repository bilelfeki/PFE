package mss.pfe.fileIntegrator.code_generation;

public class RepositoryCode {

    public static String RepositorySourceCode=
            "package mss.pfe.fileIntegrator.entities;\n" +
                    "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                    "public interface " + "%1$s"+"Repo extends JpaRepository<"+"%1$s"+", Long> {\n" +
                    "}";

}
