package org.araport.image.application;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.image.domain.Db;
import org.araport.image.domain.DbXref;
import org.araport.image.domain.DbXrefSource;
import org.araport.image.executors.TaskExecutorConfig;
import org.araport.image.flow.beans.FlowBeans;
import org.araport.image.listeners.ItemFailureLoggerListener;
import org.araport.image.listeners.LogProcessListener;
import org.araport.image.listeners.LogStepStartStopListener;
import org.araport.image.listeners.ProtocolListener;
import org.araport.image.policy.ExceptionSkipPolicy;
import org.araport.image.policy.PolicyBean;
import org.araport.image.rowmapper.beans.RowMapperBeans;
import org.araport.image.staging.BatchSchemaInitTasklet;
import org.araport.image.staging.ImageModuleInitTasklet;
import org.araport.image.tasklet.business.ContentDownLoaderTasklet;
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

@Configuration
@EnableBatchProcessing
@Import({ DataSourceInfrastructureConfiguration.class, 
		//RowMapperBeans.class,
		TaskExecutorConfig.class,
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
	public static final String DATA_FILES_DOWNLOAD_STEP = "dataFilesDownloadStep";

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
	ContentDownLoaderTasklet contentDownLoaderTasklet;
	
	@Autowired
	ImageModuleInitTasklet imageModuleInitTasklet;

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
				 .next(imageModuleInitStep())
				// .next(imageFilesDownLoadStep())
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
	public Step imageModuleInitStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(CHADO_IMAGE_MODULE_INITIALIZATION_STEP);

		Step step = stepBuilder.tasklet(imageModuleInitTasklet).build();

		return step;

	}
	
	@Bean
	public Step imageFilesDownLoadStep() {

		StepBuilder stepBuilder = stepBuilders
				.get(DATA_FILES_DOWNLOAD_STEP);

		Step step = stepBuilder.tasklet(contentDownLoaderTasklet).build();

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
