package dao;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.CountSchedulerMapper;
import logic.Exchange;

@Repository
public class ConuntSchedulerDao {
	@Autowired
	private SqlSessionTemplate template;
	private Class<CountSchedulerMapper> cls = CountSchedulerMapper.class;
	
	public void insert(Exchange ex) {
		template.getMapper(cls).insert(ex);
	}
}
