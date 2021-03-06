package org.araport.image.reader;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.dao.impl.DbDaoImpl;
import org.araport.image.domain.DbXref;
import org.araport.image.rowmapper.DbXrefRowMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("reader")
@Import({DataSourceInfrastructureConfiguration.class, DbDaoImpl.class})
public class DbXrefItemReader {
	
	@Autowired 
	DataSource targetDataSource;
	
	
	@Bean
	public ItemReader<DbXref> dbReader(){
		
		JdbcCursorItemReader<DbXref> reader = new JdbcCursorItemReader<DbXref>();
		String sql = "select db_id, accession as primary_accession, 'X' as secondary_accession, version, description from chado.dbxref limit 2";
		
		reader.setSql(sql);
		reader.setDataSource(targetDataSource);
		reader.setRowMapper(rowMapper());
		
		return reader;
	}
	
	@Bean
	public DbXrefRowMapper rowMapper(){
		return new DbXrefRowMapper();
	}
}
