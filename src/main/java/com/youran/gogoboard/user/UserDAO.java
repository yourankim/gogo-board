package com.youran.gogoboard.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.youran.gogoboard.HelpSqlSessionTemplate;

import lombok.Data;

@Repository
@Data
public class UserDAO extends HelpSqlSessionTemplate {
	
	public List<UserVO> selectAllUsers() throws Exception {
		return getSqlSessionTemplate().selectList("users.selectAllUsers");
	}
}
