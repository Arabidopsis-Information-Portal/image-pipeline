package org.araport.image.listeners;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.common.ApplicationConstants;
import org.araport.image.dao.GeneralDao;
import org.araport.image.dao.impl.DbDaoImpl;
import org.araport.image.dao.impl.GeneralDaoImpl;
import org.araport.image.rowmapper.beans.RowMapperBeans;
import org.araport.image.utils.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component("source_stockproperty_listener")
@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class,
		GeneralDaoImpl.class, RowMapperBeans.class })
public class StagingStockPropertiesStepListener implements
		StepExecutionListener {

	private static final String TRUNCATE_STAGING_STOCKPROP_SQL_PATH = "/sql/transformations/stock_properties/truncate_staging.stockprop.sql";
	private static final String TRUNCATE_STAGING_STOCKPROP_SQL = FileUtils
			.getSqlFileContents(TRUNCATE_STAGING_STOCKPROP_SQL_PATH);

	private static final Logger log = Logger
			.getLogger(StagingStockPropertiesStepListener.class);

	@Autowired
	DataSource targetDataSource;

	private GeneralDao generalDao;

	@Override
	public ExitStatus afterStep(StepExecution execution) {
		log.info("Paging Item Reader Step Test: " + execution.getStepName()
				+ " has ended!");

		ExecutionContext jobContext = execution.getJobExecution()
				.getExecutionContext();

		return execution.getExitStatus();
	}

	@Override
	public void beforeStep(StepExecution execution) {

		log.info("Paging Item Reader Step: " + execution.getStepName()
				+ " has begun!");

		ExecutionContext stepContext = execution.getExecutionContext();

		stepContext.put("partitionName", execution.getStepName());

		log.info("Partition: " + execution.getStepName()
				+ "; Id Range: " + " MinValue: "
				+ stepContext.get("minValue") + " MaxValue: "
				+ stepContext.get("maxValue"));

		// 1. Truncating Staging StockProp
       
		/*
		log.info("Injected Truncate Staging Stock Properties SQL:"
				+ TRUNCATE_STAGING_STOCKPROP_SQL);
		log.info("Truncate Staging Stock Properties Table...");

		generalDao.executeSQL(TRUNCATE_STAGING_STOCKPROP_SQL);
		*/
		

	}

	@PostConstruct
	public void setDao() {

		this.generalDao = new GeneralDaoImpl();
		this.generalDao.setDataSource(targetDataSource);

	}
}
