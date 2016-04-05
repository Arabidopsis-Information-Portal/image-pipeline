package org.araport.image.application;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.stock.domain.Db;
import org.araport.stock.domain.DbXref;
import org.araport.stock.domain.DbXrefSource;
import org.araport.stock.domain.SourceStockDrivingQuery;
import org.araport.stock.domain.Stock;
import org.araport.stock.domain.StockProperty;
import org.araport.stock.domain.StockPropertySource;
import org.araport.stock.executors.TaskExecutorConfig;
import org.araport.stock.flow.beans.FlowBeans;
import org.postgresql.util.PSQLException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.PartitionStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.araport.stock.listeners.ItemFailureLoggerListener;
import org.araport.stock.listeners.LogProcessListener;
import org.araport.stock.listeners.LogStepStartStopListener;
import org.araport.stock.listeners.ProtocolListener;
import org.araport.stock.policy.ExceptionSkipPolicy;
import org.araport.stock.policy.PolicyBean;
import org.araport.stock.rowmapper.beans.RowMapperBeans;
import org.araport.stock.tasklet.staging.BatchSchemaInitTasklet;

@Configuration
@EnableBatchProcessing
@Import({ DataSourceInfrastructureConfiguration.class, 
		RowMapperBeans.class, TaskExecutorConfig.class,
		FlowBeans.class, PolicyBean.class })
@PropertySources(value = { @PropertySource("classpath:/partition.properties") })
public class LoadStocksJobBatchConfiguration {

	private static final Logger log = Logger
			.getLogger(LogStepStartStopListener.class);

	// Main Job
	public static final String MAIN_JOB = "mainJob";

	// Application Schemas
	public static final String BATCH_SCHEMA_INITIALIZATION_STEP = "batchSchemaInitStep";
	public static final String CHADO_IMAGE_MODULE_INITIALIZATION_STEP = "chadoImageModuleInitStep";

	@Autowired
	private Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private JobBuilderFactory jobBuilders;

	@Autowired
	protected StepBuilderFactory stepBuilders;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
		
	@Autowired
	BatchSchemaInitTasklet batchSchemaInitTasklet;

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private TaskExecutor concurrentExecutor;

	@Autowired
	ExceptionSkipPolicy exceptionSkipPolicy;

	@Autowired
	JobExplorer jobExplorer;

	@Autowired
	DataSource targetDataSource;

	public JobExplorer getJobExplorer() throws Exception {
		return jobExplorer;
	}

	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(targetDataSource);
		factory.setTransactionManager(getTransactionManager());
		factory.afterPropertiesSet();
		return (JobRepository) factory.getObject();
	}

	public PlatformTransactionManager getTransactionManager() throws Exception {
		return new DataSourceTransactionManager(targetDataSource);
	}

	// tag::jobstep[]
	@Bean
	public Job loadStocks() throws Exception {
		return jobs
				.get(MAIN_JOB)
				.listener(protocolListener())
				 .start(batchSchemaInitStep())
				 .build();
	}


	@Bean
	public Step batchSchemaInitStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(BATCH_SCHEMA_INITIALIZATION_STEP);

		Step step = stepBuilder.tasklet(batchSchemaInitTasklet).build();

		return step;

	}
	
	@Bean
	public ProtocolListener protocolListener() {
		return new ProtocolListener();
	}

	@Bean
	public LogProcessListener logProcessListener() {
		return new LogProcessListener();
	}

	@Bean
	public LogStepStartStopListener stepStartStopListener() {
		return new LogStepStartStopListener();
	}

	@Bean
	ItemFailureLoggerListener itemFailureListener() {
		return new ItemFailureLoggerListener();
	}

}
