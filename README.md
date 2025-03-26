# Project Name and Objective 

## Project Name: Medix

## Objective

Medix is a state-of-the-art hospital management system designed to enhance the operational efficiency of healthcare services. It is built on the robust Java Spring Boot framework, ensuring reliable performance and ease of maintenance. Utilizing JWT for secure authentication, Medix safeguards sensitive medical data, providing a trusted environment for users.

The system is uniquely equipped with real-time video call capabilities, integrating seamlessly with Daily.co to facilitate virtual consultations between patients and doctors. This feature is essential in extending healthcare reach and providing convenient care delivery, especially in remote or underserved areas. Moreover, Medix employs the innovative WhisperAPI for automatic transcription of these video consultations into text summaries, thereby aiding in accurate medical record-keeping and reducing administrative overhead.



## Target Audience

The primary users of Medix are hospitals, clinics, and healthcare providers looking to modernize their service delivery and improve patient care through technology. By integrating Medix into their operations, healthcare facilities can achieve improved operational efficiency, enhanced data security, and provide superior patient-centric care.

# Guide: Setting Up the Medix Hospital Management System on a Local Machine

## Software Requirements
To successfully set up the Medix system, ensure the following tools and dependencies are installed:
- Java JDK 17 or higher: Essential for running Java applications.
- Spring Boot framework: Provides the infrastructure for building and running Spring applications.
- JWT (JSON Web Token) for authentication: Handles secure authentication mechanisms.
- Daily.co API: Integrates video call functionalities.
- WhisperAPI: Converts video recordings into text summaries.
- Database system (recommended: MySQL or PostgreSQL): Stores and manages application data.
- Maven: Manages project dependencies and builds.

## Step-by-Step Setup Guide

### 1. Cloning the Repository
Start by cloning the Medix repository to your local machine. Open your terminal and run:
```
git clone https://github.com/YourRepository/Medix.git
```



### 2. Installing Dependencies
Navigate to the project directory and use Maven to install all necessary dependencies:
```
cd Medix
mvn install
```



### 3. Configuring the Environment
Proper configuration is vital for the application to run smoothly. Follow these steps:

#### Set up the `.env` file
Create a `.env` file in the root directory of the project and specify the necessary environment variables:
- JWT_SECRET: A secret key for JWT to sign and verify the tokens.
- DAILY_CO_API_KEY: Your Daily.co API key for video call functionalities.
- WHISPER_API_KEY: Your WhisperAPI key for video-to-text conversion.
- DATABASE_URL: Your database connection string.

Example `.env` file:
```type:Generated,lang:Environment Variables,path:,lines:0-0
JWT_SECRET=your_jwt_secret
DAILY_CO_API_KEY=your_dailyco_api_key
WHISPER_API_KEY=your_whisperapi_key
DATABASE_URL=jdbc:mysql://localhost:3306/medixdb
```



#### Configure the database
Ensure your database is set up according to the connection string specified in the `.env` file. Execute the necessary SQL scripts to create tables and schemas as defined in the project’s documentation.

### 4. Running the Application
With all configurations set, run the application using:
```
mvn spring-boot:run
```


## Common Pitfalls and Troubleshooting Tips
- **Dependency Errors**: If Maven fails to resolve dependencies, check your network connection or the configuration of your Maven settings.
- **JWT Secret Key**: Make sure the JWT secret key is long and complex enough to ensure security.
- **API Keys**: Invalid Daily.co or WhisperAPI keys will prevent those services from functioning correctly. Verify that the keys are correct and active.
- **Database Connectivity**: Issues connecting to the database can often be traced back to incorrect credentials or URLs in the `.env` file. Double-check these entries for accuracy.

By following these steps, developers can set up the Medix system on their local machines without prior knowledge of the project, paving the way for a smooth development and testing process.
# Before Running the Medix Hospital Management System: Required Changes

To ensure the Medix system operates correctly and securely, you must configure several settings and integrate external services. Below is a checklist of required changes and detailed setup instructions for critical integrations.

## Checklist of Configuration Changes

1. **JWT Secret Key**
   - Set a secure key to sign and verify JWT tokens used for authentication.

2. **Database Credentials**
   - Provide the username, password, and URL for the database connection.

3. **Daily.co API Key**
   - Obtain and configure the API key needed for video call functionality.

4. **WhisperAPI Integration Key**
   - Set the key required for integrating the WhisperAPI service for video-to-text conversion.

## Detailed Instructions

### i. Setting up the WhisperAPI Project

The WhisperAPI project is essential for converting video consultations into text summaries, which are crucial for maintaining accurate and searchable medical records. Follow these steps to set up the WhisperAPI:

1. **Clone the Project**
   - Open your terminal and run:
     ```bash
     git clone https://github.com/AdityaBondre/WhisperAPI.git
     ```

2. **Install Dependencies**
   - Navigate to the project directory and install the required dependencies:
     ```bash
     cd WhisperAPI
     pip install -r requirements.txt
     ```

3. **Configure the API Key**
   - Open the settings file and replace `YOUR_API_KEY` with your WhisperAPI integration key:
     ```python
     API_KEY = "YOUR_API_KEY"
     ```

4. **Run the WhisperAPI Server**
   - Execute the following command to start the server:
     ```bash
     python app.py
     ```

### ii. Configuring Daily.co for Video Calls

Daily.co is used to initiate and record video consultations, offering an easy-to-use API that integrates seamlessly into the Medix system.

1. **Obtain the API Key**
   - Visit the Daily.co API dashboard.
   - Create a new project or select an existing one.
   - Navigate to the project settings and copy the API key.

2. **Integrate the API Key**
   - Open your project's configuration file where API keys are stored.
   - Replace `YOUR_DAILY_CO_KEY` with the key you copied:
     ```java
     DAILY_CO_KEY = "YOUR_DAILY_CO_KEY"
     ```

3. **Test the Integration**
   - Ensure that the Daily.co API key is working correctly by initiating a test call through your application's interface.

By following these steps, you can ensure that the Medix Hospital Management System is fully configured and ready for secure and effective operation. Consider testing each component thoroughly to confirm everything is set up properly.

# Use HTML Page for Testing and Developing New Frontend

## Overview of the HTML Page in the Resources Folder

The HTML page located in the resources folder, specifically the [`doctor-dashboard.html`](src/main/resources/templates/doctor-dashboard.html) file, serves a dual purpose. It acts as a testing platform for video call functionalities provided by Daily.co and as a scaffold for developing the frontend interface for the Medix system. This page includes various interactive elements, such as buttons for managing appointments and pop-ups for video calls, which can be extended or modified during frontend development.

## Instructions for Using the HTML Page

### Testing Video-Related Functionality

To test video call features using Daily.co integrated into the HTML page, follow these steps:

1. **Open the HTML Page**
   - Load the [`doctor-dashboard.html`](src/main/resources/templates/doctor-dashboard.html) in a web browser or through your development server.

2. **Navigate to Video Call Section**
   - Find the video call controls which are part of the video call container. This is initialized but hidden on page load.

3. **Start a Video Call**
   - Click the "Join Video Call" button which triggers the [`joinVideoCall`](src/main/resources/templates/doctor-dashboard.html#L320-L352) function. This function fetches a video call URL and displays it within an iframe.

   ```type:Quoted,lang:JavaScript,path:src/main/resources/templates/doctor-dashboard.html,lines:340-341
   
      document.getElementById("videoCallFrame").src = data.videoCallUrl;
      document.getElementById("videoCallContainer").style.display = "block";
      

   ```


4. **Test Recording Functionality**
   - Utilize the "Start Recording" button to test the recording features. This button activates the local media devices and starts recording the audio streams.

### Developing and Integrating a New Frontend

To use this HTML page as a basis for developing and integrating a new frontend, follow these steps:

1. **Study the Existing Structure**
   - Analyze the existing HTML and JavaScript code to understand the layout and functionalities. Pay attention to the modal pop-ups and form elements.

2. **Modify HTML and CSS**
   - Adjust the HTML structure and CSS styles according to the new design requirements. For instance, add new form fields or redesign the layout using CSS grid or flexbox.

3. **Extend JavaScript Functionality**
   - Add or modify JavaScript functions to enhance the interactivity of the page. For example, integrate new APIs or improve existing event handlers.

4. **Test Changes Locally**
   - Continuously test your changes by reloading the HTML page in the browser and checking for functionality and design accuracy.

## Key Features and Limitations

- **Features**: The HTML page includes setup for testing real-time interactions like video calls and dynamic content updates through JavaScript.
- **Limitations**: The page is tightly coupled with the backend APIs, meaning any significant changes to backend logic may require corresponding frontend adjustments.

By following these instructions, developers can effectively utilize the [`doctor-dashboard.html`](src/main/resources/templates/doctor-dashboard.html) page for both testing and development purposes, ensuring a robust and user-friendly frontend for the Medix Hospital Management System.
# APIs of All Controllers in Medix Hospital Management System

The Medix system features a variety of APIs across different controllers, each serving specific functions. Below is a detailed list organized by controller.

### Patient Controller APIs

- **GET /api/patients/appointments/{appointmentId}/video-call**
  - Description: Fetches the URL for a video call if the appointment is virtual and approved.
  - Input: `appointmentId` (path variable).
  - Output: Video call URL.
  
- **GET /api/patients/search**
  - Description: Searches for doctors based on optional criteria.
  - Input Parameters: `gender`, `specialization`, `hospitalName`, `name` (query parameters).
  - Output: List of [`DoctorDTO`](src/main/java/com/WhoKnows/Medix/controller/PatientController.java#L67-L75).

- **POST /api/patients/request**
  - Description: Patients can request an appointment.
  - Input: [`AppointmentRequestDTO`](src/main/java/com/WhoKnows/Medix/controller/PatientController.java#L80).
  - Output: Confirmation message.

- **DELETE /api/patients/{id}/cancel-by-patient**
  - Description: Allows a patient to cancel their own appointment.
  - Input: `id` (path variable).
  - Output: Confirmation message.

- **GET /api/patients/patient**
  - Description: Retrieves all appointments for a logged-in patient.
  - Output: List of [`AppointmentDTO`](src/main/java/com/WhoKnows/Medix/controller/PatientController.java#L95-L105).

- **GET /api/patients/medical-history**
  - Description: Fetches the medical history for a logged-in patient.
  - Output: List of [`MedicalHistoryDTO`](src/main/java/com/WhoKnows/Medix/controller/PatientController.java#L108-L118).

### Doctor Controller APIs

- **POST /api/doctors/convert-audio/{appointmentId}**
  - Description: Converts audio file to text and stores summary in medical history.
  - Input: `file` (multipart file), `appointmentId` (path variable).
  - Output: Summary of conversation.

- **GET /api/doctors/appointments/{appointmentId}/video-call**
  - Description: Provides video call access for an approved virtual appointment.
  - Input: `appointmentId` (path variable).
  - Output: Video call URL.

- **GET /api/doctors/status**
  - Description: Retrieves the current status of the authenticated doctor.
  - Output: Doctor's status.

- **PUT /api/doctors/{id}/approve**
  - Description: Approves a pending appointment by setting a time.
  - Input: `id` (path variable), `time` (part of request body).
  - Output: Confirmation message.

- **PUT /api/doctors/{id}/reject**
  - Description: Rejects a pending appointment.
  - Input: `id` (path variable).
  - Output: Confirmation message.

- **DELETE /api/doctors/{id}/cancel-by-doctor**
  - Description: Allows a doctor to cancel an appointment.
  - Input: `id` (path variable).
  - Output: Confirmation message.

- **PUT /api/doctors/{id}/reschedule**
  - Description: Reschedules an existing appointment.
  - Input: `id` (path variable), [`RescheduleAppointmentDTO`](src/main/java/com/WhoKnows/Medix/controller/DoctorController.java#L197-L204).
  - Output: Confirmation message.

- **GET /api/doctors/doctor**
  - Description: Retrieves all appointments for the authenticated doctor based on status and date.
  - Input: `status`, `appointmentDate` (query parameters).
  - Output: List of [`AppointmentDTO`](src/main/java/com/WhoKnows/Medix/controller/DoctorController.java#L207-L230).

- **GET /api/doctors/medical-history/{patientId}**
  - Description: Fetches medical history for a specific patient.
  - Input: `patientId` (path variable).
  - Output: List of [`MedicalHistoryDTO`](src/main/java/com/WhoKnows/Medix/controller/DoctorController.java#L233-L245).

### Auth Controller APIs

- **GET /api/auth/signup**, **/login**, **/patient/dashboard**, **/admin/dashboard**, **/doctor/dashboard**, **/doctor/profile-update**, **/patient/profile-update**
  - Description: Serves respective HTML pages for authentication and dashboard access.

- **POST /api/auth/login**
  - Description: Authenticates user and provides session tokens.
  - Input: [`User`](src/main/java/com/WhoKnows/Medix/controller/AuthController.java#L66-L73).
  - Output: Session token and user role.

- **POST /api/auth/register**
  - Description: Registers a new user in the system.
  - Input: [`User`](src/main/java/com/WhoKnows/Medix/controller/AuthController.java#L77-L81).
  - Output: Confirmation message.

- **PUT /api/auth/profile/{id}**
  - Description: Updates user profile based on role.
  - Input: `id` (path variable), [`UserProfileUpdateDTO`](src/main/java/com/WhoKnows/Medix/controller/AuthController.java#L85-L95).

- **GET /api/auth/profile/details**
  - Description: Retrieves details of the logged-in user based on their role.
  - Output: User details.

### Admin Controller APIs

- **GET /api/admin/doctors**, **/patients**
  - Description: Retrieves lists of doctors or patients based on query parameters or all.
  - Output: List of [`DoctorDTO`](src/main/java/com/WhoKnows/Medix/controller/AdminController.java#L39-L93) or [`PatientDTO`](src/main/java/com/WhoKnows/Medix/controller/AdminController.java#L72-L93).

- **PUT /api/admin/doctors/{id}/approve**, **/reject**
  - Description: Approves or rejects a doctor based on their id.
  - Input: `id` (path variable).
  - Output: Confirmation message.

- **POST /api/admin/doctors/{doctorId}/send-profile-update-email**
  - Description: Sends an email to a doctor to update their profile.
  - Input: `doctorId` (path variable).
  - Output: Confirmation message or error.

These APIs form the backbone of the Medix Hospital Management System, enabling seamless functionality across different modules and user roles.
# Medix Hospital Management System File Structure

The Medix Hospital Management System is designed to streamline the interaction between patients, doctors, and administrators within a healthcare setting. Below is a breakdown of the project's main directories and files, explaining their purpose, inputs, and outputs where applicable.

```
Medix/
├── src/
│   ├── main/
│   │   ├── java/com/WhoKnows/Medix/
│   │   │   ├── config/                # Configuration settings for security and application setup
│   │   │   ├── controller/            # Manages HTTP request handling for different roles
│   │   │   ├── model/                 # Entity definitions for database interaction
│   │   │   │   ├── DTOs/              # Data Transfer Objects for structured data exchange
│   │   │   ├── repository/            # Interfaces for database access and queries
│   │   │   ├── security/              # Security configurations and JWT handling
│   │   │   ├── service/               # Business logic and service layer implementation
│   │   │   └── MedixApplication.java  # Main application entry point
│   │   └── resources/
│   │       ├── templates/             # HTML templates for different views and dashboards
│   │       └── application.properties # Application properties and configurations
│   └── test/
│       └── java/com/WhoKnows/Medix/   # Test cases for various components
└── README.md                          # General information and setup instructions
```

### Directory and File Details

- **`src/main/java/com/WhoKnows/Medix/`**
  - **Purpose**: Contains all the Java source code for the application, structured in packages.
  
- **`config/`**
  - **Purpose**: Holds configuration classes for security and application setup.
  - **Output**: Configures application context and security settings.
  
- **`controller/`**
  - **Purpose**: Handles routing and processing of HTTP requests for different application functionalities.
  - **Input**: HTTP requests from users.
  - **Output**: HTTP responses based on processed business logic.
  
- **`model/`**
  - **Purpose**: Contains Java classes that represent the database models and DTOs for data transfer.
  - **Output**: Defines the structure of data for database operations and data exchange.
  
- **`repository/`**
  - **Purpose**: Provides Spring Data JPA interfaces for CRUD operations on database entities.
  - **Input**: Database connection and entity models.
  - **Output**: Database querying capabilities.
  
- **`security/`**
  - **Purpose**: Configures security settings, including JWT authentication mechanisms.
  - **Output**: Security filters and authentication providers that secure the application.
  
- **`service/`**
  - **Purpose**: Contains business logic and service layer implementations for managing entities.
  - **Input**: Requests from controllers to perform operations.
  - **Output**: Processed data that can be sent back to controllers.
  
- **`MedixApplication.java`**
  - **Purpose**: The main entry point for the Spring Boot application.
  - **Output**: Boots up the Spring application context.

- **`src/main/resources/`**
  - **Purpose**: Contains static resources and application properties.
  
- **`templates/`**
  - **Purpose**: Stores HTML templates for rendering user interfaces.
  - **Output**: Dynamic web pages served to the users.

- **`application.properties`**
  - **Purpose**: Configures application-wide properties like database settings, server port, and more.
  
- **`src/test/`**
  - **Purpose**: Contains test cases for unit testing different components of the application.
  - **Input**: Test cases and mock data.
  - **Output**: Test results and coverage.

- **`README.md`**
  - **Purpose**: Provides an overview of the project, setup instructions, and other necessary documentation.

This structured approach not only helps in maintaining the project efficiently but also aids new developers in understanding the system architecture quickly.
# Medix Application Architecture Overview

## Introduction

Medix is a healthcare management system designed to streamline the process of booking and managing appointments, handling medical records, and facilitating communication between patients, doctors, and administrators. The system is built with a focus on security, user-friendliness, and efficiency. This document provides a high-level overview of the Medix application architecture, highlighting the core components and their interactions.

## System Architecture

Medix is a Spring Boot application, leveraging the robust Spring ecosystem for security, data management, and RESTful service construction. The application is structured into several key layers:

- **Controller Layer**: Handles HTTP requests and responses, orchestrating the flow of data between the frontend and the service layer.
- **Service Layer**: Contains business logic, making decisions, processing data, and calling repositories to interact with the database.
- **Repository Layer**: Manages data persistence and retrieval from the database using Spring Data JPA.
- **Model Layer**: Defines the data entities and their relationships that are mapped to the database tables.
- **Security Layer**: Manages authentication and authorization, ensuring secure access to resources.

## Key Components

### Controllers

- **AuthController**: Manages user authentication and registration processes.
- **PatientController**: Handles patient-specific actions such as booking and managing appointments.
- **DoctorController**: Manages doctor-specific functionalities including approving or rejecting appointments.
- **AdminController**: Provides administrative functionalities like managing doctor approvals and accessing system-wide data.

### Services

- **UserService**: Manages user-related operations such as registration, login, and profile updates.
- **AppointmentService**: Handles all operations related to appointment management.
- **DoctorService**: Contains business logic related to doctors, such as searching and managing doctor profiles.
- **EmailService**: Handles sending emails for various user actions like appointment notifications and registration confirmations.

### Repositories

- **UserRepository**: Abstracts the database operations for user data.
- **DoctorRepository**: Manages data persistence for doctors.
- **PatientRepository**: Manages data persistence for patients.
- **AppointmentRepository**: Manages appointment records.
- **MedicalHistoryRepository**: Manages medical history records.

### Models

- **User**: Base class for system users including common fields like name, email, and password.
- **Doctor**, **Patient**, **Admin**: Extend the User model with specific properties and behavior pertinent to their roles.
- **Appointment**: Represents an appointment, containing details such as date, time, and status.
- **MedicalHistory**: Contains historical medical data for patients.

### Security Configuration

- **SecurityConfig**: Configures security aspects such as URL access restrictions based on roles, password encoding, and JWT management.
- **JwtUtil**: Utility class for creating and parsing JWT tokens.
- **CustomUserDetailsService**: Implements user details retrieval logic by interacting with the database.

## Data Flow

1. **User Registration and Authentication**: Users register through the `AuthController`, which interacts with `UserService` to persist user data. Authentication is handled via JWT tokens generated and parsed by `JwtUtil`.
   
2. **Appointment Booking**: Patients request appointments through `PatientController`, which uses `AppointmentService` to process and store appointments in the database.

3. **Appointment Management**: Doctors review and manage appointments through `DoctorController`, with `AppointmentService` facilitating the retrieval and update of appointment records.

4. **Administrative Functions**: Admins use `AdminController` to perform administrative tasks such as approving doctors, managed through `DoctorService`.

## Conclusion

The Medix application architecture is designed to be modular and scalable, separating concerns to allow for maintainability and extensibility. Each component has a well-defined role, ensuring a clean and organized codebase that adheres to the principles of clean architecture. This structure supports the ongoing growth and addition of features to the Medix system, making it a robust solution for healthcare management.
# Conclusion: Medix Hospital Management System

The Medix Hospital Management System represents a significant advancement in the integration of healthcare and technology. By providing a cohesive platform that includes user authentication, role-based access, real-time video consultations via Daily.co, and automated text summaries through WhisperAPI, Medix streamlines the day-to-day operations of hospital management. The system's architecture supports a secure, modular framework that promotes efficient workflow between patients, doctors, and administrators.

Reflecting on the importance of Medix, it becomes clear that this system goes beyond mere administrative assistance. It enhances the quality of healthcare delivery by ensuring that critical health services are both accessible and effective. Real-time interactions and accurate, automated documentation reduce the potential for human error and ensure that treatments are delivered promptly and appropriately.

Looking forward, Medix is set to incorporate more innovative features such as AI-based diagnostic tools and comprehensive patient analytics. These enhancements will not only improve the accuracy of diagnoses and treatment plans but will also provide valuable insights into health trends and outcomes. This ongoing development indicates a commitment to continuously improving the quality of healthcare services, making Medix a crucial tool in the evolution of hospital management.

The Medix Hospital Management System thus stands out as a beacon of progress in healthcare technology, promising to transform traditional hospital environments into more efficient, patient-focused communities.
#  Author Details

## Full Name
- **Name**: Aditya Bondre

## GitHub Username
- **GitHub**: [Aditya Bondre](https://github.com/AdityaBondre/Medix)

## Email Address
- **Email**: abondre299@gmail.com

## LinkedIn Profile
- **LinkedIn**: [Aditya Bondre](https://www.linkedin.com/in/aditya-bondre-78a623299/)

## Invitation for Connection and Collaboration
I am always looking for opportunities to collaborate and connect with like-minded professionals and enthusiasts in the tech community. If you are interested in discussing potential projects, exchanging ideas, or simply connecting for a chat about technology, please feel free to reach out to me via email or LinkedIn. I am excited to explore opportunities that enhance our learning and contribute to the tech community. Let's connect!
