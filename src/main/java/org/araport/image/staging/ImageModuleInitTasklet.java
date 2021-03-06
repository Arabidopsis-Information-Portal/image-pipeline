package org.araport.image.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.dao.GeneralDao;
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
@Import({ DataSourceInfrastructureConfiguration.class, GeneralDaoImpl.class })
public class ImageModuleInitTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(ImageModuleInitTasklet.class);

	private static final String IMAGE_MODULE_INIT_SQL_PATH = "/sql/schema/chado/init_chado_image_module.sql";
	private static final String IMAGE_MODULE_INIT_SQL = FileUtils
			.getSqlFileContents(IMAGE_MODULE_INIT_SQL_PATH);

	@Autowired
	Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;

	@Override
	public RepeatStatus execute(StepContribution step, ChunkContext context)
			throws Exception {

		log.info("Injected Init Image Module SQL:" + IMAGE_MODULE_INIT_SQL);
		log.info("Initializing Image Module ...");

		generalDao.executeSQL(IMAGE_MODULE_INIT_SQL);

		return RepeatStatus.FINISHED;

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}

}
