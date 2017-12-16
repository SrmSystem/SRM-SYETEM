package com.qeweb.modules.persistence;

import java.sql.Types;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.type.StringType;

public class Oracle10gDialectEx extends Oracle10gDialect{
	
	public Oracle10gDialectEx(){
		super();
		this.registerHibernateType(Types.NVARCHAR, StringType.INSTANCE.getName() );
	}

}
