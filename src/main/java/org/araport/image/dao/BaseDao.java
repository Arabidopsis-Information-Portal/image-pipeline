package org.araport.image.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class BaseDao {

	protected SimpleJdbcTemplate simpleJdbcTemplate;

	public void setDataSource(javax.sql.DataSource dataSource) {
	    this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}
	
}
