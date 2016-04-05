package org.araport.image.processor;

import org.apache.log4j.Logger;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.dao.DbDao;
import org.araport.image.dao.impl.DbDaoImpl;
import org.araport.image.domain.Db;
import org.araport.image.domain.DbXref;
import org.araport.image.listeners.LogProcessListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ DataSourceInfrastructureConfiguration.class, DbDaoImpl.class })
public class DbXrefItemProcessor implements ItemProcessor<DbXref, DbXref> {

	@Autowired
	private DbDao dbDao;

	private static final Logger log = Logger
			.getLogger(DbXrefItemProcessor.class);

	@Override
	public DbXref process(final DbXref dbxref) throws Exception {

		Db tairStockDb = dbDao.findDbByName("TAIR Stock");

		if (tairStockDb != null) {
			log.info("TAIR Stock Db" + tairStockDb);
		} else {
			log.info("TAIR Stock not found!");
		}

		return dbxref;
	}

	public void setDbDao(DbDao dbDao) {
		this.dbDao = dbDao;
	}

}
