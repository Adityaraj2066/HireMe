package com.aditya.jobApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.aditya.jobApplication.model.JobApplication;
import com.aditya.jobApplication.model.JobPost;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.from-name:Job Application System}")
    private String fromName;

    /**
     * Send email notification when a job application is submitted
     */
    public void sendApplicationConfirmationEmail(JobApplication application) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getApplicantEmail());
            message.setSubject("Application Confirmation - " + application.getJobPost().getPostProfile());
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Thank you for applying to the position of %s at %s.\n\n" +
                "Your application has been received and is currently under review.\n\n" +
                "Application Details:\n" +
                "- Job Position: %s\n" +
                "- Company: %s\n" +
                "- Application Status: %s\n" +
                "- Applied Date: %s\n\n" +
                "We will review your application and get back to you soon.\n\n" +
                "Best regards,\n" +
                "%s",
                application.getApplicantName(),
                application.getJobPost().getPostProfile(),
                application.getJobPost().getCompanyName(),
                application.getJobPost().getPostProfile(),
                application.getJobPost().getCompanyName(),
                application.getStatus(),
                application.getAppliedAt(),
                fromName
            );
            
            message.setText(emailBody);
            mailSender.send(message);
        } catch (Exception e) {
            // Log error but don't throw exception to avoid breaking the application flow
            System.err.println("Failed to send application confirmation email: " + e.getMessage());
        }
    }

    /**
     * Send email notification when application status is updated
     */
    public void sendStatusUpdateEmail(JobApplication application, String previousStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(application.getApplicantEmail());
            message.setSubject("Application Status Update - " + application.getJobPost().getPostProfile());
            
            String statusMessage = getStatusMessage(application.getStatus());
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Your application status for the position of %s at %s has been updated.\n\n" +
                "Application Details:\n" +
                "- Job Position: %s\n" +
                "- Company: %s\n" +
                "- Previous Status: %s\n" +
                "- Current Status: %s\n\n" +
                "%s\n\n" +
                "Best regards,\n" +
                "%s",
                application.getApplicantName(),
                application.getJobPost().getPostProfile(),
                application.getJobPost().getCompanyName(),
                application.getJobPost().getPostProfile(),
                application.getJobPost().getCompanyName(),
                previousStatus,
                application.getStatus(),
                statusMessage,
                fromName
            );
            
            message.setText(emailBody);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send status update email: " + e.getMessage());
        }
    }

    /**
     * Send email notification when a new job is posted (optional - for subscribers)
     */
    public void sendNewJobNotificationEmail(String recipientEmail, String recipientName, JobPost jobPost) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(recipientEmail);
            message.setSubject("New Job Opportunity - " + jobPost.getPostProfile() + " at " + jobPost.getCompanyName());
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "A new job opportunity has been posted that might interest you!\n\n" +
                "Job Details:\n" +
                "- Position: %s\n" +
                "- Company: %s\n" +
                "- Posted Date: %s\n" +
                "- Status: %s\n\n" +
                "Visit our platform to learn more and apply!\n\n" +
                "Best regards,\n" +
                "%s",
                recipientName,
                jobPost.getPostProfile(),
                jobPost.getCompanyName(),
                jobPost.getPostingDate(),
                jobPost.getRegistrationStatus(),
                fromName
            );
            
            message.setText(emailBody);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send new job notification email: " + e.getMessage());
        }
    }

    /**
     * Get a personalized message based on application status
     */
    private String getStatusMessage(String status) {
        switch (status.toUpperCase()) {
            case "REVIEWED":
                return "Your application has been reviewed by our team. We will contact you soon with further updates.";
            case "INTERVIEW":
                return "Congratulations! You have been shortlisted for an interview. Our team will contact you shortly to schedule the interview.";
            case "SELECTED":
                return "Congratulations! You have been selected for this position. Our HR team will contact you with further details.";
            case "REJECTED":
                return "Thank you for your interest. Unfortunately, we are unable to proceed with your application at this time. We encourage you to apply for other positions that match your profile.";
            default:
                return "Your application is being processed. We will keep you updated on any changes.";
        }
    }
}

