# Uni-Event

**Uni-Event** is a modern, full-stack University Event Management system built with Spring Boot, Thymeleaf, Bootstrap, and PostgreSQL. It streamlines university event scheduling, student management, and user authentication with a professional, user-friendly web interface.

---

## Features

### User Authentication & Profile
- **Signup & Login:** Secure registration and login with hashed passwords.
- **Profile Management:** Users can update their first name, last name, email, and password.
- **Database-backed Users:** All user data is stored in PostgreSQL (no in-memory users).
- **Session-based Security:** Only authenticated users can access the app.
- **Logout:** Secure logout from the navbar.

### Event Management
- **Add/Edit/Delete Events:** Create, update, and remove events with details like name, location, date, time, and capacity.
- **Event List & Filtering:** View all events, filter by date, and see event details.
- **Event Registration:** Register students for events, with capacity checks and attendee lists.
- **Attendee Management:** View and manage event attendees.

### Student Management
- **Add/Edit/Delete Students:** Manage student records with unique Student ID, name, age, and department.
- **Search, Filter, Sort:** Find students by ID or name, filter and sort the student list.

### Modern UI/UX
- **Thymeleaf & Bootstrap:** Responsive, professional design with modals, cards, and modern forms.
- **Navigation:** Clean navbar with Home, Students, Events, Profile, and Logout.
- **Flash Messages:** User feedback for actions (success, error, etc.).
- **Profile Page:** Avatar, sectioning, and easy-to-use update form.

### Database
- **PostgreSQL:** All data is persisted in a PostgreSQL database.
- **JPA/Hibernate:** Automatic schema management and entity relationships.

---

## Technologies Used

- **Java 17+**
- **Spring Boot** (Web, Security, Data JPA)
- **Spring Security** (Authentication & Authorization)
- **Spring Data JPA** (ORM)
- **Thymeleaf** (Server-side HTML rendering)
- **Bootstrap 5** (Responsive UI)
- **PostgreSQL** (Database)
- **Hibernate** (JPA implementation)
- **Lombok** (for model boilerplate)
- **Maven** (Build tool)
- **Jakarta Persistence API** (JPA annotations)
- **Bootstrap Icons** (UI icons)

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone <your-repo-url>
cd University-Event-Management-main
```

### 2. Configure PostgreSQL

- Ensure PostgreSQL is installed and running.
- Create a database:
  ```sql
  CREATE DATABASE unieventdb;
  ```
- (Optional) Create a user and grant privileges:
  ```sql
  CREATE USER unieventuser WITH PASSWORD 'yourpassword';
  GRANT ALL PRIVILEGES ON DATABASE unieventdb TO unieventuser;
  ```

### 3. Update `application.properties`

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/unieventdb
spring.datasource.username=unieventuser
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Build and Run

#### Using Maven:
```bash
./mvnw spring-boot:run
```
or
```bash
mvn spring-boot:run
```

#### Or run from your IDE (IntelliJ, Eclipse, VS Code, etc.)

---

## Usage

1. **Access the app:**  
   Open [http://localhost:8080](http://localhost:8080) in your browser.

2. **Sign up:**  
   Register a new account with your first name, last name, email, username, and password.

3. **Login:**  
   Use your credentials to log in.

4. **Profile:**  
   Click the Profile button in the navbar to update your details.

5. **Manage Events & Students:**  
   - Add, edit, or delete events and students.
   - Register students for events.
   - View attendees and event details.

6. **Logout:**  
   Use the Logout button in the navbar to securely log out.

---

## Project Structure

```
src/
  main/
    java/
      com.geekster.project.assignment.UniversityEventManagement/
        Controller/      # Web and API controllers
        Model/           # JPA entities (User, Student, Event, etc.)
        Repository/      # Spring Data JPA repositories
        Service/         # Business logic services
        UniversityEventManagementApplication.java
    resources/
      templates/         # Thymeleaf HTML templates
      application.properties
  test/
    java/
      ...                # Unit and integration tests
```

---

## Customization & Extensibility

- **Roles & Permissions:** Easily add user roles (admin, student, etc.) with Spring Security.
- **Email Verification:** Integrate email for signup confirmation or password reset.
- **Event Analytics:** Add charts and analytics for event participation.
- **Calendar View:** Integrate a calendar for event scheduling.
- **API Endpoints:** RESTful APIs for integration with other systems or a mobile app.

---

**Enjoy using Uni-Event! If you have questions or want to contribute, feel free to open an issue or pull request.**
