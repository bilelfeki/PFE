package mss.pfe.fileIntegrator.code_generation;

public class EntityCode {
    public static  String DefaultEntitySourceCode =
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
}
