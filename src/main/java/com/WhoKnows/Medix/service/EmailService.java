package com.WhoKnows.Medix.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // mail for  successfully register
    public void sendRegistrationEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🎉 Welcome to Medix - Your Healthcare Companion! 🏥");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>Welcome to Medix, " + name + "!</h2>"
                    + "<p>Dear " + name + ",</p>"
                    + "<p>We are delighted to have you onboard! 🎉 Medix is here to simplify your healthcare experience.</p>"
                    + "<p>With Medix, you can:</p>"
                    + "<ul>"
                    + "<li>Find and book appointments with top doctors.</li>"
                    + "<li>Access your medical history and reports anytime.</li>"
                    + "<li>Manage your health records effortlessly.</li>"
                    + "</ul>"
                    + "<p><b>Start exploring Medix today and take control of your healthcare journey!</b></p>"
                    + "<p>If you have any questions, feel free to contact our support team.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }


    // mail for approved doctor
    public void sendDoctorApprovalEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("✅ Congratulations, Dr. " + name + "! Your Medix Verification is Complete");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>Welcome to Medix, Dr. " + name + "!</h2>"
                    + "<p>Dear Dr. " + name + ",</p>"
                    + "<p>We are pleased to inform you that your verification process has been successfully completed. 🎉</p>"
                    + "<p>You are now an approved doctor on <b>Medix</b> and can start receiving appointments from patients.</p>"
                    + "<p>With Medix, you can:</p>"
                    + "<ul>"
                    + "<li>Manage and schedule patient appointments seamlessly.</li>"
                    + "<li>Access patient medical histories with ease.</li>"
                    + "<li>Enhance patient engagement through our platform.</li>"
                    + "</ul>"
                    + "<p><b>Login to your Medix account and start providing your expert care today!</b></p>"
                    + "<p>If you have any questions or need assistance, feel free to contact our support team.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }

    // mail for reject doctor
    public void sendDoctorRejectionEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("❌ Medix Verification Update for Dr. " + name);

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #C0392B; text-align: center;'>Verification Update for Dr. " + name + "</h2>"
                    + "<p>Dear Dr. " + name + ",</p>"
                    + "<p>We regret to inform you that your verification process for Medix has been <b>unsuccessful</b> at this time. Unfortunately, we were unable to approve your profile due to certain verification criteria not being met.</p>"
                    + "<p>If you believe this decision was made in error or if you wish to provide additional information, you may reapply or contact our support team for further assistance.</p>"
                    + "<p>We appreciate your interest in Medix and hope to work with you in the future.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }


    // mail for booking of appointment
    public void sendAppointmentBookingEmail(String to, String patientName, String doctorName, LocalDate appointmentDate,String appointmentType) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("📅 New Appointment Booking - Approval Required");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>New Appointment Request</h2>"
                    + "<p>Dear Dr. " + doctorName + ",</p>"
                    + "<p>A new appointment request has been made by <b>" + patientName + "</b>.</p>"
                    + "<p><b>Appointment Details:</b></p>"
                    + "<ul>"
                    + "<li><b>Patient Name:</b> " + patientName + "</li>"
                    + "<li><b>Appointment Date:</b> " + appointmentDate + "</li>"
                    + "<li><b>Appointment Type:</b> " + appointmentType + "</li>"
                    + "</ul>"
                    + "<p><b>⚠️ Please note:</b> The appointment is pending your approval. Kindly review and approve it at your earliest convenience.</p>"
                    + "<p><b>⏳ Appointment Time:</b> The appointment time will be provided by you after approval.</p>"
                    + "<p>To manage your appointments, please log in to your Medix dashboard.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //mail for failure of booking
    public void sendAppointmentFailureEmail(String to, LocalDate appointmentDate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("⚠️ Appointment Booking Failed - Technical Issue");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #C0392B; text-align: center;'>Appointment Booking Failed</h2>"
                    + "<p>Dear ,</p>"
                    + "<p>We regret to inform you that your appointment </b> on <b>" + appointmentDate + "</b> could not be completed due to technical issues.</p>"
                    + "<p>We sincerely apologize for the inconvenience caused. Our technical team is working to resolve the issue.</p>"
                    + "<p><b>What You Can Do:</b></p>"
                    + "<ul>"
                    + "<li>Try booking the appointment again after some time.</li>"
                    + "<li>If the issue persists, please contact our support team for assistance.</li>"
                    + "</ul>"
                    + "<p>We appreciate your patience and understanding.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }


    //mail for appointment approval
    public void sendAppointmentApprovalEmail(String to, String patientName, String doctorName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("✅ Appointment Confirmed - Dr. " + doctorName);

            // Convert LocalDate and LocalTime to proper DateTime format for Google Calendar
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

            String startDateTime = appointmentDate.format(dateFormatter) + "T" + appointmentTime.format(timeFormatter) + "00";
            String endDateTime = appointmentDate.format(dateFormatter) + "T" + appointmentTime.plusMinutes(30).format(timeFormatter) + "00"; // 30-minute duration

            // Google Calendar Event Link
            String calendarUrl = "https://www.google.com/calendar/render?action=TEMPLATE"
                    + "&text=Appointment+with+Dr.+"
                    + URLEncoder.encode(doctorName, StandardCharsets.UTF_8)
                    + "&dates=" + startDateTime + "/" + endDateTime
                    + "&details=" + URLEncoder.encode("Appointment confirmed with Dr. " + doctorName, StandardCharsets.UTF_8)
                    + "&location=Online"
                    + "&sf=true&output=xml";

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>Appointment Confirmed</h2>"
                    + "<p>Dear " + patientName + ",</p>"
                    + "<p>Your appointment with <b>Dr. " + doctorName + "</b> has been <b>approved</b> and scheduled.</p>"
                    + "<p><b>Appointment Details:</b></p>"
                    + "<ul>"
                    + "<li><b>Doctor Name:</b> Dr. " + doctorName + "</li>"
                    + "<li><b>Appointment Date:</b> " + appointmentDate + "</li>"
                    + "<li><b>Appointment Time:</b> " + appointmentTime + "</li>"
                    + "</ul>"
                    + "<p>📌 Please make sure to be available at the scheduled time.</p>"
                    + "<p>If you have any questions, feel free to contact our support team.</p>"
                    + "<br>"
                    + "<a href='" + calendarUrl + "' style='display: inline-block; padding: 10px 20px; color: #fff; background-color: #28a745; text-decoration: none; border-radius: 5px;'>➕ Add to Google Calendar</a>"
                    + "<br><br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    // mail for reject appointment by doctor
    public void sendAppointmentRejectionEmail(String to, String patientName, String doctorName, LocalDate appointmentDate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("❌ Appointment Request Declined - Dr. " + doctorName);

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #C0392B; text-align: center;'>Appointment Request Declined</h2>"
                    + "<p>Dear " + patientName + ",</p>"
                    + "<p>We regret to inform you that your appointment request with <b>Dr. " + doctorName + "</b> on <b>" + appointmentDate + "</b> has been <b>declined</b>.</p>"
                    + "<p>Possible reasons may include:</p>"
                    + "<ul>"
                    + "<li>The doctor is unavailable on the requested date.</li>"
                    + "<li>The appointment schedule is fully booked.</li>"
                    + "<li>Other unforeseen circumstances.</li>"
                    + "</ul>"
                    + "<p>📌 <b>What You Can Do:</b></p>"
                    + "<ul>"
                    + "<li>Try booking an appointment for another date.</li>"
                    + "<li>Check for availability with other doctors in our system.</li>"
                    + "<li>Contact our support team if you need assistance.</li>"
                    + "</ul>"
                    + "<p>We apologize for any inconvenience and appreciate your understanding.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    // mail for canceled appointment by doctor
    public void sendAppointmentCancellationEmailByDoctor(String to, String patientName, String doctorName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🚫 Appointment Cancelled - Dr. " + doctorName);

            // Format Google Calendar search query for easy manual removal
            String calendarSearchUrl = "https://calendar.google.com/calendar/u/0/r/search?q="
                    + URLEncoder.encode("Appointment with Dr. " + doctorName, StandardCharsets.UTF_8);

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #C0392B; text-align: center;'>Your Appointment Has Been Cancelled</h2>"
                    + "<p>Dear " + patientName + ",</p>"
                    + "<p>We would like to inform you that your appointment with <b>Dr. " + doctorName + "</b> on <b>" + appointmentDate + " at " + appointmentTime + "</b> has been <b>cancelled</b>.</p>"
                    + "<p>📌 <b>What You Can Do:</b></p>"
                    + "<ul>"
                    + "<li>If you need to reschedule, please book a new appointment.</li>"
                    + "<li>If this was a mistake, please contact our support team immediately.</li>"
                    + "</ul>"
                    + "<p>🚨 If you have already added this appointment to Google Calendar, please remove it:</p>"
                    + "<a href='" + calendarSearchUrl + "' style='display: inline-block; padding: 10px 20px; color: #fff; background-color: #d9534f; text-decoration: none; border-radius: 5px;'>🗑 Remove from Google Calendar</a>"
                    + "<br><br>"
                    + "<p>We apologize for any inconvenience and appreciate your understanding.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    // mail for cancel appointment by patient
    public void sendAppointmentCancellationEmailToPatient(String to, String doctorName, String patientName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("✅ Appointment Successfully Canceled - Dr. " + doctorName);

            // Format date and time for Google Calendar search
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

            String appointmentDateTime = appointmentDate.format(dateFormatter) + "T" + appointmentTime.format(timeFormatter) + "00Z";

            // Google Calendar search link to help the patient find and remove the event manually
            String calendarSearchUrl = "https://www.google.com/calendar/u/0/r/search?q="
                    + URLEncoder.encode("Appointment with Dr. " + doctorName, StandardCharsets.UTF_8);

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #C0392B; text-align: center;'>Your Appointment Has Been Canceled</h2>"
                    + "<p>Dear " + patientName + ",</p>"
                    + "<p>Your appointment with <b>Dr. " + doctorName + "</b> scheduled for <b>" + appointmentDate + " at " + appointmentTime + "</b> has been <b>successfully canceled</b>.</p>"
                    + "<p>📌 <b>Appointment Details:</b></p>"
                    + "<ul>"
                    + "<li><b>Doctor Name:</b> Dr. " + doctorName + "</li>"
                    + "<li><b>Appointment Date:</b> " + appointmentDate + "</li>"
                    + "<li><b>Appointment Time:</b> " + appointmentTime + "</li>"
                    + "</ul>"
                    + "<p>If you have added this appointment to your Google Calendar, you can remove it using the link below:</p>"
                    + "<a href='" + calendarSearchUrl + "' style='display: inline-block; padding: 10px 20px; color: #fff; background-color: #dc3545; text-decoration: none; border-radius: 5px;'>🗑 Remove from Google Calendar</a>"
                    + "<br><br>"
                    + "<p>If you wish to reschedule, you can book a new appointment through our system.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    // mail for reschedule the appointment by doctor
    public void sendAppointmentRescheduledEmail(String to, String patientName, String doctorName,
                                                LocalDate oldAppointmentDate, LocalDate newAppointmentDate,
                                                LocalTime oldAppointmentTime, LocalTime newAppointmentTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("🔄 Appointment Rescheduled - Dr. " + doctorName);

            // Format date and time for Google Calendar
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

            String newStartDateTime = newAppointmentDate.format(dateFormatter) + "T" + newAppointmentTime.format(timeFormatter) + "00Z";
            String newEndDateTime = newAppointmentDate.format(dateFormatter) + "T" + newAppointmentTime.plusMinutes(30).format(timeFormatter) + "00Z"; // 30-minute duration

            // URL to manually search and delete old appointment
            String oldCalendarSearchUrl = "https://calendar.google.com/calendar/u/0/r/search?q="
                    + URLEncoder.encode("Appointment with Dr. " + doctorName, StandardCharsets.UTF_8);

            // URL to add new rescheduled appointment
            String newCalendarUrl = "https://www.google.com/calendar/render?action=TEMPLATE"
                    + "&text=Rescheduled+Appointment+with+Dr.+" + URLEncoder.encode(doctorName, StandardCharsets.UTF_8)
                    + "&dates=" + newStartDateTime + "/" + newEndDateTime
                    + "&details=" + URLEncoder.encode("Your appointment has been rescheduled with Dr. " + doctorName, StandardCharsets.UTF_8)
                    + "&location=Online"
                    + "&sf=true&output=xml";

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #2E86C1; text-align: center;'>Your Appointment Has Been Rescheduled</h2>"
                    + "<p>Dear " + patientName + ",</p>"
                    + "<p>We would like to inform you that your appointment with <b>Dr. " + doctorName + "</b> has been <b>rescheduled</b>.</p>"
                    + "<p>📌 <b>Updated Appointment Details:</b></p>"
                    + "<ul>"
                    + "<li><b>Previous Date:</b> " + oldAppointmentDate + "</li>"
                    + "<li><b>New Date:</b> " + newAppointmentDate + "</li>"
                    + "<li><b>Previous Time:</b> " + oldAppointmentTime + "</li>"
                    + "<li><b>New Time:</b> " + newAppointmentTime + "</li>"
                    + "</ul>"
                    + "<p>🚨 Please remove your previous appointment from Google Calendar before adding the new one:</p>"
                    + "<br>"
                    + "<a href='" + oldCalendarSearchUrl + "' style='display: inline-block; padding: 10px 20px; color: #fff; background-color: #d9534f; text-decoration: none; border-radius: 5px;'>🗑 Remove Old Appointment</a>"
                    + "<br><br>"
                    + "<p>✅ Once removed, you can add the updated appointment:</p>"
                    + "<a href='" + newCalendarUrl + "' style='display: inline-block; padding: 10px 20px; color: #fff; background-color: #28a745; text-decoration: none; border-radius: 5px;'>📅 Add New Appointment</a>"
                    + "<br><br>"
                    + "<p>We apologize for any inconvenience and appreciate your understanding.</p>"
                    + "<p>📞 If you have any questions or need further assistance, please contact our support team.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    // mail for doctor by management to update profile
    public void sendProfileUpdateEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject("⚠️ Profile Update Required - Complete Your Medix Profile");

            String emailContent = "<html><body>"
                    + "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 10px; max-width: 600px; margin: auto;'>"
                    + "<h2 style='color: #D9534F; text-align: center;'>Profile Update Required, " + name + "!</h2>"
                    + "<p>Dear " + name + ",</p>"
                    + "<p>We noticed that your profile has not been updated yet. To proceed with your approval, please update your profile as soon as possible.</p>"
                    + "<p>Updating your profile will allow you to:</p>"
                    + "<ul>"
                    + "<li>Verify your credentials and get approved quickly.</li>"
                    + "<li>Start receiving patient appointments.</li>"
                    + "<li>Enhance your visibility on our platform.</li>"
                    + "</ul>"
                    + "<p><b>Click the link below to update your profile:</b></p>"
                    + "<p style='text-align: center;'><a href='https://medix.com/doctor/profile-update' "
                    + "style='display: inline-block; background: #5BC0DE; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>"
                    + "Update Profile Now</a></p>"
                    + "<p>If you have already updated your profile, please ignore this email.</p>"
                    + "<br>"
                    + "<p>Best regards,</p>"
                    + "<p><b>The Medix Team</b></p>"
                    + "<hr style='border: 0; height: 1px; background: #ddd;'>"
                    + "<p style='font-size: 12px; color: gray; text-align: center;'>This is an automated email, please do not reply.</p>"
                    + "</div>"
                    + "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Log error
        }
    }




}
