package org.araport.stock.tasklet.staging;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.GeneralDao;
import org.araport.stock.dao.impl.GeneralDaoImpl;
import org.araport.stock.tasklet.business.BulkLoadStockPropertiesTasklet;
import org.araport.stock.utils.FileUtils;
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
public class StagingStockCVTermTruncateTasklet implements Tasklet {

	private static final Log log = LogFactory
			.getLog(StagingStockCVTermTruncateTasklet.class);
	
	private static final String TRUNCATE_STAGING_STOCK_CVTERM_SQL_PATH = "/sql/transformations/stock_cvterm/truncate_staging_stock_cvterm.sql";
	private static final String TRUNCATE_STAGING_STOCK_CVTERM_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_STAGING_STOCK_CVTERM_SQL_PATH);
	
	private static final String REFRESH_STAGING_STOCK_CVTERM_MVVIEW_SQL_PATH = "/sql/transformations/stock_cvterm/refresh_stock_cvterm_mv_views.sql";
	private static final String REFRESH_STAGING_STOCK_CVTERM_MVVIEW_SQL = FileUtils
			.getSqlFileContents(REFRESH_STAGING_STOCK_CVTERM_MVVIEW_SQL_PATH);

	
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
		
		// 1. Truncating Staging Stock CVTerm
	       
			log.info("Injected Truncate Staging Stock CVTerm SQL:"
					+ TRUNCATE_STAGING_STOCK_CVTERM_SQL);
			log.info("Truncate Staging Stock CVTerm Table...");

			generalDao.executeSQL(TRUNCATE_STAGING_STOCK_CVTERM_SQL);
			
		// 2. Refreshing Stock CVTern MVVIews
		       
				log.info("Injected Refresh Staging Stock CVTerm MVViews SQL:"
						+ TRUNCATE_STAGING_STOCK_CVTERM_SQL);
				log.info("Refresh Staging Stock CVTerm MVViews...");

				generalDao.executeSQL(REFRESH_STAGING_STOCK_CVTERM_MVVIEW_SQL);
			
			return RepeatStatus.FINISHED;
			
	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}
	
}
