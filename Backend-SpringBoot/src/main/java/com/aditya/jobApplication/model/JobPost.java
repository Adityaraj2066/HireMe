package com.aditya.jobApplication.model;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity
@JsonIgnoreProperties(value = {"postId"}, allowSetters = false) // Ignore postId during JSON deserialization
public class JobPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	@Setter(AccessLevel.NONE) // Prevent Lombok from generating setter
	private int postId;
	
	// Custom setter - ignore postId during JSON deserialization to avoid issues with empty strings
	@JsonIgnore
	public void setPostId(int postId) {
		this.postId = postId;
	}
	private String companyName;
	private String companyLogoText;
	private String postProfile; 
	private String postDesc;
	private Integer reqExperience;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "job_post_tech_stack", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "tech")
	private List<String> postTechStack;
	private String postingDate;
	private String registrationStatus; // "Reg. Open", "Reg. Closed", "In Progress"
	private String registrationDeadline; // Date for countdown timer
	private String applicationStatus; // "Eligible to Apply", "Not Eligible", "Application Submitted", "Not Shortlisted"

	// Additional fields from the front-end Create form
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "job_post_responsibilities", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "responsibility")
	private List<String> rolesAndResponsibilities;
	private String qualification;
	private String specialization;
	private String yearOfPassingUG;
	private String yearOfPassingPG;
	private String workMode;
	private String probationPeriod;
	private String jobLocation;
	private String interviewLocation;
	private String interviewDate;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "job_post_interview_rounds", joinColumns = @JoinColumn(name = "post_id"))
	@Column(name = "round")
	private List<String> interviewRounds;
	private String salaryDetails;

	// Qualification percentages and CGPA fields
	private String qualificationPercent10th;
	private String qualificationPercent12th;
	private String qualificationPercentUG;
	private String qualificationPercentPG;
	private String qualificationCGPAUG;
	private String qualificationCGPAPG;

	private String genderPreference;
	private String bondDetails;
	private String backlog;
	
	
}
