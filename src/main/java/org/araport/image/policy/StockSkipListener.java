package org.araport.image.policy;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araport.image.application.DataSourceInfrastructureConfiguration;
import org.araport.image.dao.SkipItemsDao;
import org.araport.image.dao.impl.CVDaoImpl;
import org.araport.image.dao.impl.CVTermDaoImpl;
import org.araport.image.dao.impl.DbXrefDaoImpl;
import org.araport.image.dao.impl.SkipItemsDaoImpl;
import org.postgresql.util.PSQLException;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Import({ DataSourceInfrastructureConfiguration.class })
public class StockSkipListener extends BaseElement implements SkipListener {

	private static final Logger log = Logger
			.getLogger(StockSkipListener.class);
	
	@Autowired
	DataSource targetDataSource;
	
	private SkipItemsDao skipItemsDao;
 
    public void onSkipInProcess(Object item, Throwable t) {
        log.info("onSkipInProcess:" +item.getClass()+" "+t.getClass());
        if(t instanceof PSQLException){
        	PSQLException psqlSkipException=(PSQLException)t;
            skipItemsDao.save(createSkipElement("PROCESS", item.toString(), t.getClass().toString(), psqlSkipException));
        }
    }
 
    public void onSkipInRead(Throwable t) {
        log.info("onSkipInRead:"+ t.getClass());
        if(t instanceof PSQLException){
        	PSQLException psqlSkipException=(PSQLException)t;
            skipItemsDao.save(createSkipElement("READ", "", t.getClass().toString(), psqlSkipException));
        }
    }
 
    public void onSkipInWrite(Object item, Throwable t) {
        log.info("onSkipInWrite:" +item.getClass()+" "+t.getClass());
        if(t instanceof PSQLException){
        	PSQLException sampleSkipException=(PSQLException)t;
            skipItemsDao.save(createSkipElement("WRITE", item.toString(), t.getClass().toString(), sampleSkipException));
        }
    }
 
    private SkipItems createSkipElement(String type, String item, String msg, PSQLException sampleSkipException){
        SkipItems skipElement=new SkipItems();
        skipElement.setType(type);
        skipElement.setItem(item);
        skipElement.setMsg(msg);
        skipElement.setJobExecutionId(getJobExecutionId());
        skipElement.setStepExecutionId(getStepExecutionId());
        return skipElement;
    }
    public SkipItemsDao getSkipItemsDao() {
        return skipItemsDao;
    }
 
    public void setSkipItemsDao(SkipItemsDao skipItemsDao) {
        this.skipItemsDao = skipItemsDao;
    }
    
    @PostConstruct
	public void setDao() {
    	this.skipItemsDao = new SkipItemsDaoImpl();
    	this.skipItemsDao.setDataSource(targetDataSource);
		

	}
}


