package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleMapper;
import logic.Sale;

@Repository
public class SaleDao {
	@Autowired 
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private final Class<SaleMapper> cls = SaleMapper.class;
	
	public int getMaxSaleId() {
		return template.getMapper(cls).getMaxSaleId();
	}
	
	public void insert(Sale sale) {//sale 테이블에 데이터 추가
		template.getMapper(cls).insert(sale);
	}
	public List<Sale> list(String userid) {
		param.clear();
		return template.getMapper(cls).select(param);
	}

}
