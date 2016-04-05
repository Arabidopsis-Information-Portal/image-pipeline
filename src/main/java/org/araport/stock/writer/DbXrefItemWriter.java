package org.araport.stock.writer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.stock.dao.DbDao;
import org.araport.stock.dao.DbXRefDao;
import org.araport.stock.dao.impl.DbDaoImpl;
import org.araport.stock.dao.impl.DbXrefDaoImpl;
import org.araport.stock.domain.DbXref;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ DataSourceInfrastructureConfiguration.class, DbXrefDaoImpl.class })
public class DbXrefItemWriter implements ItemWriter<DbXref>{

	@Autowired
	DataSource targetDataSource;
	
	private DbXRefDao dbXrefDao;

	private static final Logger log = Logger
			.getLogger(DbXrefItemWriter.class);
	
	@Override
	public void write(List<? extends DbXref> items) throws Exception {
		
	
		if(items.get(0) != null){
			DbXref dbXref = items.get(0);
			
			System.out.println("DbXref" + dbXref);
		}
		
		for (DbXref item : items)  {
			dbXrefDao.merge(item);
		}
		
	}
	

	@PostConstruct
	public void setDao(){
		this.dbXrefDao = new DbXrefDaoImpl();
		this.dbXrefDao.setDataSource(targetDataSource);
	}
	
}
