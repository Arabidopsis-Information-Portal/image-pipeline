package org.araport.image.flow.beans;


import org.araport.image.staging.*;
import org.springframework.context.annotation.Bean;

public class FlowBeans {

	@Bean
	public BatchSchemaInitTasklet batchSchemaInitTasklet() {
		return new BatchSchemaInitTasklet();
	}

}
