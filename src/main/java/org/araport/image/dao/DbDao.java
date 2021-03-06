package org.araport.image.dao;

import org.araport.image.domain.Db;

public interface DbDao {

	public boolean create (Db db);
	public Db findDbByName(String name);
	public boolean save(Db db);
	
	public int getTairDBId();
	public int getTairStockDbId();
	public void setTairDbId();
	public void setTairStockDb();
	
	public void executeSQL(String sql);
	
}
