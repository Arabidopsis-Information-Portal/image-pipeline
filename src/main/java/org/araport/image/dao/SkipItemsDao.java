package org.araport.image.dao;

import javax.sql.DataSource;

import org.araport.image.policy.SkipItems;

public interface SkipItemsDao {
	public void save(SkipItems skipElement);
	public void setDataSource(DataSource datasource);
}
