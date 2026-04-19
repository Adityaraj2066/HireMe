# 🚀 HireMe – Full Stack Job Portal

A full-stack job portal application where users can register, login, post jobs, and apply for jobs. Built using React and Spring Boot with secure authentication.

---

## 🧩 Tech Stack

* **Frontend:** React.js
* **Backend:** Spring Boot (Java)
* **Security:** Spring Security + JWT
* **Database:** MySQL / MongoDB / H2
* **API:** REST APIs

---

## 🔐 Security & Authentication

* Implemented **JWT (JSON Web Token)** based authentication
* Secure login & registration system
* Password encryption using **BCrypt**
* Protected API routes using Spring Security
* Stateless authentication (no session storage)

---

## ✨ Features

* 🔐 User Authentication (Login/Register)
* 🛡️ JWT Authorization & Protected Routes
* 🧑‍💼 Job Posting System
* 📄 Apply to Jobs
* 📋 View All Job Listings
* ✏️ Edit & Delete Jobs


---

## 📁 Project Structure

```
HireMe/
│── Backend-SpringBoot
│── Frontend-React
│── .gitignore
│── README.md
```

---

## ⚙️ How to Run Locally

### 🔹 Backend (Spring Boot)

```
cd Backend-SpringBoot
mvn spring-boot:run
```

---

### 🔹 Frontend (React)

```
cd Frontend-React
npm install
npm start
```

---

## 🌐 API Endpoints (Example)

* `POST /auth/register` → Register user
* `POST /auth/login` → Login and get JWT token
* `GET /jobs` → Get all jobs
* `POST /jobs` → Create job (Protected)

---

## 🔄 Authentication Flow

1. User logs in with credentials
2. Backend verifies user details
3. JWT token is generated
4. Token is sent to frontend
5. Frontend stores token
6. Token is sent in every request header
7. Backend validates token before processing



## 🎯 Future Improvements

* Deploy backend & frontend
* Add role-based access control (Admin/User)
* Improve UI/UX
* Add job search & filters

---

## 👨‍💻 Author

**Aditya Raj**
GitHub: https://github.com/Adityaraj2066

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
