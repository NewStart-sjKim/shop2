package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.CommentMapper;
import logic.Comment;

@Repository
public class CommentDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String,Object> param = new HashMap<>();
	private Class<CommentMapper> cls = CommentMapper.class;

	public void insert(Comment comm) {
		template.getMapper(cls).insert(comm);
	}
	
	public int maxseq(int num) {
		
		return template.getMapper(cls).maxseq(num);
	}
	


	public List<Comment> list(Integer num) {
		return template.getMapper(cls).list(num);
	}

	public void delete(int num, int seq) {
		template.getMapper(cls).delete(num,seq);
	}



	public Comment selectOne(int num, int seq) {
	
		return template.getMapper(cls).selectOne(num,seq);
	}




}
