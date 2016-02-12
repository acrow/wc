var ioc = {
	config : {
		type : "org.nutz.ioc.impl.PropertiesProxy",
		fields : {
			paths : ["app.properties"]
		}
	},

	dataSource : {
		//type : "org.apache.commons.dbcp.BasicDataSource",
		type : "com.alibaba.druid.pool.DruidDataSource",
		fields : {
			driverClassName : {java :"$config.get('db.driver')"},
			url             : {java :"$config.get('db.url')"},
			username        : {java :"$config.get('db.username')"},
			password        : {java :"$config.get('db.password')"},
			validationQuery : {java :"$config.get('db.validationQuery')"},
			initialSize                   : 10,
			maxActive                     : 100,
			minIdle                       : 10,
			maxIdle                       : 20,
			maxWait                       : 60000,
			removeAbandonedTimeout        : 3600,
			timeBetweenEvictionRunsMillis : 60000,
			minEvictableIdleTimeMillis    : 300000,
			removeAbandoned               : true,
			poolPreparedStatements        : false,
			logAbandoned                  : true,
			testWhileIdle                 : true,
			testOnBorrow                  : false,
			testOnReturn                  : false
		},
		events : {
			depose :"close"
		}
	},

    dao : {
        type : "org.nutz.dao.impl.NutDao",
        fields : {
        	dataSource : {refer : 'dataSource'}
        }
    }
};