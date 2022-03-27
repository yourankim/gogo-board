package com.youran.gogoboard.post;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.youran.gogoboard.HelpSqlSessionTemplate;

@Repository
public class PostDAO extends HelpSqlSessionTemplate {

	public List<PostVO> selectPosts(PageVO pageVO) throws Exception {
		return getSqlSessionTemplate().selectList("posts.selectPosts", pageVO);
	}
	
	public PostVO selectPostById(String id) throws Exception {
		return getSqlSessionTemplate().selectOne("posts.selectPostById", id);
	}
	
	public int insertPost(PostVO postVO) throws Exception {
		return getSqlSessionTemplate().insert("posts.insertPost", postVO);
	}
	
	public int updatePost(PostVO postVO) throws Exception {
		return getSqlSessionTemplate().update("posts.updatePost", postVO);
	}
	
	public int deletePost(PostVO postVO) throws Exception {
		return getSqlSessionTemplate().delete("posts.deletePost", postVO);
	}
}
