package mss.pfe.fileIntegrator.code_generation;

import mss.pfe.fileIntegrator.entities.Champ;
import org.springframework.stereotype.Service;

@Service
public class FileInsertionCode {
    public static String repoDeclaration =
            "final %1$s %1$s ;\n";

    public static String instantiateObject ="%1$s %2$s = new %1$s() ;\n";
    public static String setObjectCode ="%s.set%s(%s";
    public static String saveObjectWithRepoCode ="%s.save(%s);\n";
    public static String FileInsertionConstructorCode =
            //%s is the repoName
            "    public Processor1(%1$s %1$s) {\n" +
                    "        this.%1$s = %1$s;\n" +
                    "    }\n";
    private static String packageCode=
            "package mss.pfe.fileIntegrator.entities;\n" ;
    private static String importCode=
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.context.annotation.Bean;\n" +
                    "import org.springframework.context.annotation.Configuration;\n" +
                    "import java.io.*;\n" ;
    private static String annotation =
            "@Configuration\n" ;
    public static String fileInsertionMethodCode ="" +
            "@Bean\n" +
            "public void readFile(){\n" +
            "String userHome = System.getProperty(\"user.home\");\n" +
            "String filePath=userHome + \"/Desktop/executable/src/main/java/mss/pfe/fileIntegrator/File\";\n" +
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
    public static String fileInsertionClassCode ="" +
            packageCode+
            importCode+
            annotation+
            "public class Processor1{\n" +
            //processorConstructor
            "%s"+
            //repoDeclaration
            "%s"+
            "public void Processor1(){\n}\n" +
            //saveObjectCode
            //handle for loop method
            "%s"+
            "}\n" +
            "}";
    public String formatFileInsertionConstructorCode(String FileInsertionConstructorCode,String repoName){
        return String.format(FileInsertionConstructorCode,repoName);
    }
    public String formatFileInsertionMethodCode(String fileInsertionMethodCode,String forLoopCode){
        return String.format(fileInsertionMethodCode, forLoopCode);
    }
    public String formatObjectSetter(String objectSetter, String champContent, Champ champ, String currentObjectName) {
        return String.format(objectSetter, currentObjectName, champ.getValeur(), champContent);
    }
    public String formatRepoDeclaration(String repoDeclarationDefaultCode, String repoName) {
        return String.format(repoDeclarationDefaultCode, repoName);
    }

}
