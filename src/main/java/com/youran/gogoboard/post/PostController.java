package com.youran.gogoboard.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/posts", produces = "application/json; charset=UTF-8")
public class PostController {

	@Autowired
	private PostService service;
	
	@GetMapping
	public ResponseEntity<PostResponse> getPosts() {
		
		PostResponse response = new PostResponse();
		
		try {	
			response.setPosts(service.getPosts());
			
		} catch(Exception e) {
			log.error("Exception in getPosts: {}", e.getMessage());
			response.setMessage("Fail...");
			return new ResponseEntity<PostResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPostDetail(@PathVariable String id) {
		
		PostResponse response = new PostResponse();
		log.debug("getPostDetail pathVariable: {}",id);
		try {	
			PostVO post = service.findPostById(id);
			response.setPost(post);
			
		} catch(Exception e) {
			log.error("Exception in getPosts: {}", e.getMessage());
			response.setMessage("Fail...");
			return new ResponseEntity<PostResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
		
	}

	
	@PostMapping
	public ResponseEntity<PostResponse> creatNewPost(@RequestBody PostVO newPost) {
		
		PostResponse response = new PostResponse();
		
		try {	
			String userId = "761b7be6-944a-42e4-8936-7d6754d29b18";
			service.createNewPost(newPost, userId);
			
		} catch(Exception e) {
			log.error("Exception in creatNewPost: {}", e.getMessage());
			response.setMessage("Fail...");
			return new ResponseEntity<PostResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMessage("Success!!");
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
		
	}
	
	@PatchMapping
	public ResponseEntity<PostResponse> updatePost(@RequestBody PostVO post) {
		
		PostResponse response = new PostResponse();
		
		try {	
			String userId = "761b7be6-944a-42e4-8936-7d6754d29b18";
			service.updatePost(post, userId);
			
		} catch(Exception e) {
			log.error("Exception in updatePost: {}", e.getMessage());
			response.setMessage("Fail...");
			return new ResponseEntity<PostResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMessage("Success!!");
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
		
	}
	
	@DeleteMapping
	public ResponseEntity<PostResponse> deletePost(@RequestBody PostVO post) {
		
		PostResponse response = new PostResponse();
		
		try {	
			String userId = "761b7be6-944a-42e4-8936-7d6754d29b18";
			service.deletePost(post, userId);
			
		} catch(Exception e) {
			log.error("Exception in deletePost: {}", e.getMessage());
			response.setMessage("Fail...");
			return new ResponseEntity<PostResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.setMessage("Success!!");
		return new ResponseEntity<PostResponse>(response,HttpStatus.OK);
		
	}
	
}
