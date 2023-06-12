package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.validation.Valid;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.UserMapper;
import logic.User;


@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private Class<UserMapper> cls = UserMapper.class;
	
	public void insert(User user) {
		template.getMapper(cls).insert(user);
	}
	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.selectOne("dao.mapper.UserMapper.select",param);
	}
	public void update(User user) {
		template.getMapper(cls).update(user);
	}
	public void delete(String userid) {
		param.put("userid", userid);
		template.getMapper(cls).delete(param);
	}
	public void chagpass(String userid, String chgpass) {
		param.put("password", chgpass);
		param.put("userid", userid);
		template.getMapper(cls).chgpass(param);
		
	}
	public List<User> userlist() {
		return template.getMapper(cls).select(null);
		
	}
	//select * from useraccount where userid in ('admin','test1')
	public List<User> list(String[] idchks) {
		param.clear();
		param.put("userids",idchks);
		return template.getMapper(cls).select(param);
	}
	public String search(User user) {
		String col = " userid";
		if(user.getUserid() != null) col = " password";
		param.clear();
		param.put("col", col);
		param.put("userid",user.getUserid());
		param.put("email",user.getEmail());
		param.put("phoneno",user.getPhoneno());
		return template.getMapper(cls).search(param);
	}
	public List<User> phoneList(String phoneno) {
		return template.getMapper(cls).phoneList(phoneno);
	}
}
