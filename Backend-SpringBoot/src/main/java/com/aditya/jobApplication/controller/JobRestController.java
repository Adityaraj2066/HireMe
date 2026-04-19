package com.aditya.jobApplication.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aditya.jobApplication.model.JobPost;
import com.aditya.jobApplication.service.JobService;

import lombok.Data;

@RestController
@CrossOrigin
public class JobRestController {
	
	private static final Logger logger = LoggerFactory.getLogger(JobRestController.class);
	
	@Autowired
	private JobService service;
	

	@GetMapping("jobPosts")
	public List<JobPost> getAllJobs() {
		return service.getAllJobs();
		
	}
	

	@GetMapping("/jobPost/{postId}")
	public JobPost getJob(@PathVariable int postId) {
		return service.getJob(postId);
	}
	
	
	@GetMapping("jobPosts/keyword/{keyword}")
	public List<JobPost> searchByKeyword(@PathVariable("keyword") String keyword) {
		 return service.search(keyword);

	}

	@PostMapping("jobPost")
	public ResponseEntity<?> addJob(@RequestBody JobPost jobPost) {
		try {
			logger.info("Received job post creation request");
			
			if (jobPost == null) {
				logger.error("Job post data is null");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse("Job post data is required"));
			}
			
			// Reset postId to 0 for new posts (to trigger auto-generation)
			jobPost.setPostId(0);
			
			logger.info("Company: {}, Profile: {}, TechStack size: {}", 
					jobPost.getCompanyName(), 
					jobPost.getPostProfile(),
					jobPost.getPostTechStack() != null ? jobPost.getPostTechStack().size() : 0);
			
			// Validate required fields
			if (jobPost.getCompanyName() == null || jobPost.getCompanyName().trim().isEmpty()) {
				logger.error("Company name is required");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse("Company name is required"));
			}
			if (jobPost.getPostProfile() == null || jobPost.getPostProfile().trim().isEmpty()) {
				logger.error("Post profile is required");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ErrorResponse("Post profile is required"));
			}
			
			// Set default values if not provided
			String postingDate = jobPost.getPostingDate();
			if (postingDate == null || postingDate.trim().isEmpty()) {
				jobPost.setPostingDate(java.time.LocalDate.now().toString());
			}
			String registrationStatus = jobPost.getRegistrationStatus();
			if (registrationStatus == null || registrationStatus.trim().isEmpty()) {
				jobPost.setRegistrationStatus("Reg. Open");
			}
			
			// Ensure Lists are initialized if null
			if (jobPost.getPostTechStack() == null) {
				jobPost.setPostTechStack(new ArrayList<>());
			}
			if (jobPost.getRolesAndResponsibilities() == null) {
				jobPost.setRolesAndResponsibilities(new ArrayList<>());
			}
			if (jobPost.getInterviewRounds() == null) {
				jobPost.setInterviewRounds(new ArrayList<>());
			}
			
			// Filter out empty strings from lists
			if (jobPost.getPostTechStack() != null) {
				jobPost.setPostTechStack(jobPost.getPostTechStack().stream()
					.filter(s -> s != null && !s.trim().isEmpty())
					.collect(Collectors.toList()));
			}
			if (jobPost.getRolesAndResponsibilities() != null) {
				jobPost.setRolesAndResponsibilities(jobPost.getRolesAndResponsibilities().stream()
					.filter(s -> s != null && !s.trim().isEmpty())
					.collect(Collectors.toList()));
			}
			if (jobPost.getInterviewRounds() != null) {
				jobPost.setInterviewRounds(jobPost.getInterviewRounds().stream()
					.filter(s -> s != null && !s.trim().isEmpty())
					.collect(Collectors.toList()));
			}
			
			logger.info("Attempting to save job post...");
			JobPost saved = service.addJob(jobPost);
			logger.info("Job post created successfully with ID: {}", saved.getPostId());
			return ResponseEntity.ok(saved);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			logger.error("Data integrity violation while creating job post", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ErrorResponse("Database constraint violation: " + e.getMessage()));
		} catch (Exception e) {
			logger.error("Error creating job post", e);
			e.printStackTrace();
			String errorMessage = e.getMessage();
			if (e.getCause() != null) {
				errorMessage += " - Cause: " + e.getCause().getMessage();
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorResponse("Error creating job post: " + errorMessage));
		}
	}
	
	@PutMapping("jobPost")
	public JobPost updateJob(@RequestBody JobPost jobPost) {
		service.updateJob(jobPost);
		return service.getJob(jobPost.getPostId());
	}
	
	
	@DeleteMapping("jobPost/{postId}")
	public String deleteJob(@PathVariable int postId)
	{
		service.deleteJob(postId);
		return "Deleted";
	}
	
	
	@GetMapping("load")
	public String loadData() {
		service.load();
		return "success";
	}

	@Data
	private static class ErrorResponse {
		private String message;

		public ErrorResponse(String message) {
			this.message = message;
		}
	}

}
