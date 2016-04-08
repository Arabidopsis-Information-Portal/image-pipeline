package org.araport.image.tasklet.business;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.dao.DatabaseImageDao;
import org.araport.image.dao.GeneralDao;
import org.araport.image.dao.impl.DatabaseImageStagingDaoImpl;
import org.araport.image.dao.impl.GeneralDaoImpl;
import org.araport.image.utils.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Import({ DataSourceInfrastructureConfiguration.class, DatabaseImageStagingDaoImpl.class })
public class StagingImageBulkLoadingTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(StagingImageBulkLoadingTasklet.class);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	@Autowired
	DatabaseImageDao dbImage;

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

		String[] sourceFiles = org.araport.image.utils.FileUtils.getAllFileNames(ApplicationConstants.DOWNLOAD_STAGING_DIR);
		
		return RepeatStatus.FINISHED;

	}

}
