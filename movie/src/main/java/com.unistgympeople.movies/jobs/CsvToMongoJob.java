package com.unistgympeople.movies.jobs;
 
import com.unistgympeople.movies.model.User;
import com.unistgympeople.movies.model.Movie;
import com.unistgympeople.movies.model.Ratings;
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
        .next(step2())
        .next(step3())
        .build();
  }
 
  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1").<User, User>chunk(10).reader(userReader())
        .writer(userWriter()).build();
  }
 
  @Bean
  public FlatFileItemReader<User> userReader() {
    FlatFileItemReader<User> reader = new FlatFileItemReader<>();
    reader.setResource(new ClassPathResource("users.csv"));
    reader.setLineMapper(new DefaultLineMapper<User>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[]{"userId", "gender", "age", "occupation", "zip-code"});
 
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
        setTargetType(User.class);
      }});
    }});
    return reader;
  }
 
  @Bean
  public MongoItemWriter<User> userWriter() {
    MongoItemWriter<User> writer = new MongoItemWriter<User>();
    writer.setTemplate(mongoTemplate);
    writer.setCollection("users");
    return writer;
  }

  @Bean
  public Step step2() {
      return stepBuilderFactory.get("step2").<Movie, Movie>chunk(10).reader(movieReader())
              .writer(movieWriter()).build();
  }

  @Bean
  public FlatFileItemReader<Movie> movieReader() {
      FlatFileItemReader<Movie> reader = new FlatFileItemReader<>();
      reader.setResource(new ClassPathResource("movie.csv"));
      reader.setLineMapper(new DefaultLineMapper<Movie>() {{
          setLineTokenizer(new DelimitedLineTokenizer() {{
              setNames(new String[]{"id", "title", "genres"});
              setDelimiter(",");
          }});
          setFieldSetMapper(new BeanWrapperFieldSetMapper<Movie>() {{
              setTargetType(Movie.class);
          }});
      }});
      return reader;
  }

  @Bean
  public MongoItemWriter<Movie> movieWriter() {
      MongoItemWriter<Movie> writer = new MongoItemWriter<Movie>();
      writer.setTemplate(mongoTemplate);
      writer.setCollection("movies");
      return writer;
  }

  @Bean
  public Step step3() {
      return stepBuilderFactory.get("step3").<Ratings, Ratings>chunk(10).reader(ratingsReader())
              .writer(ratingsWriter()).build();
  }

  @Bean
  public FlatFileItemReader<Ratings> ratingsReader() {
      FlatFileItemReader<Ratings> reader = new FlatFileItemReader<>();
      reader.setResource(new ClassPathResource("ratings.csv"));
      reader.setLineMapper(new DefaultLineMapper<Ratings>() {{
          setLineTokenizer(new DelimitedLineTokenizer() {{
              setNames(new String[]{"ratingId", "rating", "timestamp"});
              setDelimiter(",");
          }});
          setFieldSetMapper(new BeanWrapperFieldSetMapper<Ratings>() {{
              setTargetType(Ratings.class);
          }});
      }});
      return reader;
  }

  @Bean
  public MongoItemWriter<Ratings> ratingsWriter() {
      MongoItemWriter<Ratings> writer = new MongoItemWriter<Ratings>();
      writer.setTemplate(mongoTemplate);
      writer.setCollection("ratings");
      return writer;
  }


}