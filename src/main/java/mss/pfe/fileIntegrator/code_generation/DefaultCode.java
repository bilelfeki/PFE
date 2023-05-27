package mss.pfe.fileIntegrator.code_generation;

public class DefaultCode {

    private static String packageCode=
                    "package mss.pfe.fileIntegrator.entities;\n" ;
    private static String importCode=
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                            "import java.io.*;\n" ;
    private static String annotation =
            "@Configuration\n" ;
    public static String processorConstructor=
            //%s is the repoName
            "    public Processor%s(%s %s) {\n" +
                    "        this.%s = %s;\n" +
                    "    }\n";
    public static String saveObjectLine1="%s %s = new %s() ;\n";
    public static String saveObjectLine2="%s.set%s(%s";
    public static String saveObjectLine3="%s.save(%s);\n";

    // final customerRepo customerRepo;
    public static String repoDeclaration =
            "final %s %s ;\n";

    public static String finalProcessor="" +
                    packageCode+
                    importCode+
                    annotation+
                    //fileLineIndex
                    "public class Processor%s{\n" +
                    //processorConstructor
                    "%s"+
                    //repoDeclaration
                    "%s"+
                    "public void Processor%s(){\n}\n" +
                    //saveObjectCode
                    //handle for loop method
                    "%s"+
                    "}\n" +
                    "}";
    public static String forLoop="" +
        "@Bean\n" +
            "public void readFile(){\n" +
            "String filePath=\"C:/Users/bilel/Desktop/entityCreator/executable/src/main/java/mss//pfe/fileIntegrator/File\";\n" +
            "File sourceFile1 = new File(filePath).listFiles()[0];\n" +
            "try {\n" +
            "FileReader fileReader = new FileReader(sourceFile1);\n" +
            "BufferedReader reader = new BufferedReader(fileReader);\n" +
            "\n" +
            "String line;\n" +
            "while ((line = reader.readLine()) != null) {\n" +
            "System.out.println(line);\n" +
            "%s"+
            "}\n" +
            "} catch (FileNotFoundException e) {\n" +
            "throw new RuntimeException(e);\n" +
            "} catch (IOException e) {\n" +
            "throw new RuntimeException(e);\n" +
            "}\n" +
            "\n" +
            "\n" ;
}
