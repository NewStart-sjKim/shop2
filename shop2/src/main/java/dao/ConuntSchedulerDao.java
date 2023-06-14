package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.CountSchedulerMapper;
import dao.mapper.UserMapper;

@Repository
public class ConuntSchedulerDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private Class<CountSchedulerMapper> cls = CountSchedulerMapper.class;
	
	public void insert(List<List<String>> trlist) {
		template.getMapper(cls).insert(null);
	}
}
