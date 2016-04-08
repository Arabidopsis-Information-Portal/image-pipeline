package org.araport.image.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.dao.DatabaseImageDao;
import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.domain.DbXref;
import org.araport.image.staging.BatchSchemaInitTasklet;
import org.araport.image.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;


@Component("database_image_staging_dao")
@Import({ DataSourceInfrastructureConfiguration.class })
public class DatabaseImageStagingDaoImpl implements DatabaseImageDao{

	@Autowired
	DataSource targetDataSource;
	
	private static final Log log = LogFactory
			.getLog(DatabaseImageStagingDaoImpl.class);

	private static final String INSERT_SQL_PATH = "/sql/schema/image/insert_image_data.sql";
	private static final String INSERT_SQL = FileUtils
			.getSqlFileContents(INSERT_SQL_PATH);
	
	private static final String UPDATE_SQL_PATH = "/sql/schema/image/upadte_image_data.sql";
	private static final String UPDATE_SQL = FileUtils
			.getSqlFileContents(UPDATE_SQL_PATH);
	
	private NamedParameterJdbcOperations namedParameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public boolean create(DatabaseFileImage image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(DatabaseFileImage image) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void merge(DatabaseFileImage image) {
		
		log.info("Database Operation Merge Image Record: " + image);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", image.getName());
		params.put("filename", image.getFileName());
		params.put("extension",image.getFileExtension());
		params.put("size", image.getSize());
		params.put("width", image.getWidth());
		params.put("height", image.getHeight());
		params.put("content", image.getContent());
		params.put("md5checksum", image.getMdCheckSum());
		
		int updatedCount = namedParameterJdbcTemplate.update(UPDATE_SQL, params);
		
		log.info("Database Operation Merge Image Record. Updated Count: " + updatedCount + " " +image);
		
		if (updatedCount == 0){ //perform insert
			
			log.info("Database Operation Merge Image Record. Perform insert." + image);
			
			namedParameterJdbcTemplate.update(INSERT_SQL, params);
		}
		
	}

	@Override
	public DbXref mergeAndReturn(DatabaseFileImage image) {
		// TODO Auto-generated method stub
		return null;
	}

		
	@PostConstruct
	public void setDataSource() {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				targetDataSource);
		this.jdbcTemplate = new JdbcTemplate(targetDataSource);
			
	}
	
	@Override
	public void setDataSource(DataSource datasource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				datasource);
		this.jdbcTemplate = new JdbcTemplate(datasource);
		
		
	}

}
