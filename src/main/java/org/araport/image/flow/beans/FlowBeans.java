package org.araport.image.flow.beans;


import org.araport.image.staging.*;
import org.araport.image.tasklet.business.ContentDownLoaderTasklet;
import org.springframework.context.annotation.Bean;

public class FlowBeans {

	@Bean
	public BatchSchemaInitTasklet batchSchemaInitTasklet() {
		return new BatchSchemaInitTasklet();
	}

	@Bean
	public ContentDownLoaderTasklet contentDownLoaderTasklet(){
		return new ContentDownLoaderTasklet();
	}
}
