package mss.pfe.fileIntegrator.code_writer;

import mss.pfe.fileIntegrator.code_generation.DefaultCode;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Service
public class CodeWriter {
    public CodeWriter() {
    }
    public void writeContentIntoPath(String content,String path,String fileName){
        try {
            Writer writer1 = new FileWriter(path +fileName + ".java");
            writer1.write(content);
            writer1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
