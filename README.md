# Bed Tracker and Booking System
***
Below is application design diagram:

![image](https://github.com/deekshamypersonal/bedtracker/assets/150110347/50de1f49-910e-4853-bf3c-7f7a559e8338)

***

**Process Flow:**
* Patients register themselves by providing personal information, including their identity number and email.
* The patient receives a token on their email ID upon registering, which they must click to complete the registration process.
* Patients log in and search for available beds in a hospital belonging to their city.
* If available, they book the bed and upload the COVID report. The report temporarily gets loaded into the database before approval.
* Admin gets email notification for bed request. He can approve/reject the service.
* If approved, the COVID report is uploaded to AWS S3 and deleted from the database. The available bed count for the hospital decreases by 1, and a unique ID is generated 
  and emailed to the patient.
* If the patient fails to arrive before 8 pm, booking automatically gets cancelled notifying user with an email.
* If rejected, patient request is removed from database, although their credentials remain valid.
  
***

**Technology Stack**
* Backend- Springboot, Spring Data JPA, Hibernate, ,Spring Security, JWT, MySQL
* RabbitMq message broker for asynchronous communication to email notification service
* Eureka Discovery Server as registry service
* Spring cloud gateway as a load balancer
* Redis as distributed cache
* Aws S3 for uploading files


