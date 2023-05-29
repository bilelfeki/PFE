package mss.pfe.fileIntegrator.code_generation;

import mss.pfe.fileIntegrator.entities.SpringBatchCodeGenerated;
import org.springframework.stereotype.Service;

@Service
public class SpringBatchCode {
    public SpringBatchCode() {
    }

    public static String flatFileItemReader="" +
            "\t@Bean\n" +
            "\tpublic FlatFileItemReader<%1$s> reader() {\n" +
            "\t\tFixedLengthTokenizer tokenizer = new FixedLengthTokenizer() ;\n" +
            "\t\ttokenizer.setColumns(columnList);\n" +
            "\t\ttokenizer.setNames(listName);\n" +
            "\n" +
            "\t\treturn new FlatFileItemReaderBuilder<%1$s>()\n" +
            "\t\t\t.name(\"%1$sReader\")\n" +
            "\t\t\t.resource(new ClassPathResource(\"%2$s\"))\n" +
            "\t\t\t\t.lineTokenizer(tokenizer)\n" +
            "\t\t\t.fieldSetMapper(new BeanWrapperFieldSetMapper<%1$s>() {{\n" +
            "\t\t\t\tsetTargetType(%1$s.class);\n" +
            "\t\t\t}})\n" +
            "\t\t\t.build();\n" +
            "\t}\n";
    public static String processor="" +
            "/* Processor Code*/\n" +
            "public class  %1$sItemProcessor implements ItemProcessor<%1$s, %1$s> {\n" +
            "\n" +
            "\t@Override\n" +
            "\t\treturn  %1$s;\n" +
            "\t}\n" +
            "}";
    public static String job="" +
            "/* job Code */\n" +
            "\t@Bean\n" +
            "\tpublic Job %1$sJob(JobRepository jobRepository,\n" +
            "\t\t Step step1) {\n" +
            "\t\treturn new JobBuilder(\"%1$s\", jobRepository)\n" +
            "\t\t\t.incrementer(new RunIdIncrementer())\n" +
            "\t\t\t.flow(step1)\n" +
            "\t\t\t.end()\n" +
            "\t\t\t.build();\n" +
            "\t}\n";
    public static String step="" +
            "\t@Bean\n" +
            "\tpublic Step step1(JobRepository jobRepository,\n" +
            "\t\t\tPlatformTransactionManager transactionManager, JdbcBatchItemWriter<%1$s> writer) {\n" +
            "\t\treturn new StepBuilder(\"step1\", jobRepository)\n" +
            "\t\t\t.<%1$s, %1$s> chunk(1, transactionManager)\n" +
            "\t\t\t.reader(reader())\n" +
            "\t\t\t.processor(processor())\n" +
            "\t\t\t.writer(writer)\n" +
            "\t\t\t.build();\n" +
            "\t}";
    public static String writer="" +
            "    @Bean\n" +
            "    public JpaItemWriter<%1$s> writer() {\n" +
            "        JpaItemWriter<%1$s> itemWriter = new JpaItemWriter<>();\n" +
            "        itemWriter.setEntityManagerFactory(entityManagerFactory);" +
            "        return itemWriter;\n" +
            "    }";
    public String formatFlatFileItemReader(String objectName,String fileName){
        return String.format(flatFileItemReader,objectName,fileName) ;
    }

    public String formatProcessor(String className){
        return String.format(processor,className);
    }
    public String formatJob(String objectName){
        return String.format(job,objectName);
    }
    public String formatStep(String objectName){
        return String.format(step,objectName);
    }
    public String formatWriter(String objectName){
        return String.format(writer,objectName);
    }
    public SpringBatchCodeGenerated formatAll(String className, String fileName){
        String processor = this.formatProcessor(className) ;
        String writer = this.formatWriter(className);
        String step = this.formatStep(className) ;
        String reader = this.formatFlatFileItemReader(className,fileName) ;
        String job =this.formatJob(className) ;
        return  new SpringBatchCodeGenerated(processor,writer,job,step,reader) ;
    }
}
