package com.youran.gogoboard.user;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.youran.gogoboard.HelpSqlSessionTemplate;


@Repository
public class UserDAO extends HelpSqlSessionTemplate {
	
	public List<UserVO> selectAllUsers() throws Exception {
		return getSqlSessionTemplate().selectList("users.selectAllUsers");
	}
	
	public int insertUser(UserVO userVO) throws Exception {
		return getSqlSessionTemplate().insert("users.insertUser", userVO);
	}
}
