package org.araport.image.application;

import javax.sql.DataSource;

public interface InfrastructureConfiguration {

		public abstract DataSource dataSource();
}
