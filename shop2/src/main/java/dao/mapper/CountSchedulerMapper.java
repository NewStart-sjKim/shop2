package dao.mapper;

public interface CountSchedulerMapper {
	@Insert( "insert into exchange (eno,code,name,"
			+ " primeamt,sellamt,butamt,edate) values"
			+ " (#{eno},#{code},#{name},"
			+ " #{primeamt},#{sellamt},#{butamt},#{edate})")
	void insert(
			)
}
