package org.araport.image.writer;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.domain.DatabaseFileImage;
import org.araport.image.utils.FileUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;


@Component("staging_image_writer")
@Import({ DataSourceInfrastructureConfiguration.class})
public class StagingImageItemWriter {
	
	@Autowired
	DataSource targetDataSource;
	
	private static final Log log = LogFactory.getLog(StagingImageItemWriter.class);
	
	private static final String INSERT_NAMED_PARAM_SQL_PATH = "/sql/schema/image/insert_named_param_image_data.sql";
	
	private static final String INSERT_NAMED_PARAM_SQL = FileUtils.getSqlFileContents(INSERT_NAMED_PARAM_SQL_PATH);
	
	@Bean
	public ItemWriter<DatabaseFileImage> stagingImageWriter(){
		
		JdbcBatchItemWriter<DatabaseFileImage> itemWriter = new JdbcBatchItemWriter<DatabaseFileImage>();
		itemWriter.setSql(INSERT_NAMED_PARAM_SQL);
		itemWriter.setDataSource(targetDataSource);
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<DatabaseFileImage>());
		return itemWriter;
		
	}

}
