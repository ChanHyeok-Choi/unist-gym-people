package com.unistgympeople.movies.jobs;
 
import com.unistgympeople.movies.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
 
@EnableBatchProcessing
@Configuration
public class CsvToMongoJob {
 
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private StepBuilderFactory stepBuilderFactory;
 
  @Autowired
  private MongoTemplate mongoTemplate;
 
  @Bean
  public Job readCSVFile() {
    return jobBuilderFactory.get("readCSVFile").incrementer(new RunIdIncrementer()).start(step1())
        .build();
  }
 
  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1").<User, User>chunk(10).reader(reader())
        .writer(writer()).build();
  }
 
  @Bean
  public FlatFileItemReader<User> reader() {
    FlatFileItemReader<User> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("user.csv"));
    reader.setLineMapper(new DefaultLineMapper<User>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[]{"id"});
 
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
        setTargetType(User.class);
      }});
    }});
    return reader;
  }
 
  @Bean
  public MongoItemWriter<User> writer() {
    MongoItemWriter<User> writer = new MongoItemWriter<User>();
    writer.setTemplate(mongoTemplate);
    writer.setCollection("users");
    return writer;
  }


}