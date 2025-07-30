## 📘 IJAA
* **Title:** SRS for IIT Jahangirnagar University Alumni Association Web Application
* **Version:** 1.0
* **Author:** Md Sahal
* **Date:** 2025-06-09

---

## 1. 📌 Introduction

### 1.1 Purpose

The purpose of this document is to define the functional and non-functional requirements of the Alumni Association Web Application for IIT Jahangirnagar University. This system will allow alumni to sign up, connect with peers, manage profiles, participate in events, form groups, and communicate via text, audio, and video.

### 1.2 Scope

This is a web-based system developed using React (frontend) and Spring Boot (backend). It will serve alumni of IIT Jahangirnagar University and will include:

* Authentication (Email/Google/Facebook)
* Profile management
* Group and event features
* Real-time chat (group and 1-to-1)
* Audio/Video calling
* Alumni search system

---

## 2. 👤 User Roles

* **Guest**: Can browse landing page and sign up.
* **Registered Alumni**: Can access all features after login.
* **Admin**: Manages events, groups, and handles reports.

---

## 3. ✅ Functional Requirements

### 3.1 Authentication & Authorization

#### 3.1.1 Sign Up

* Alumni can sign up via:

    * Email and password
    * Google OAuth
    * Facebook OAuth
* Mandatory fields: Name, Email, Password, Date of Birth, Batch Year.

#### 3.1.2 Sign In

* Alumni can log in using:

    * Email + password
    * Google or Facebook
* Forgot password with OTP/email reset link

#### 3.1.3 Session Management

* JWT tokens for authenticated sessions
* Refresh tokens for session continuity

---

### 3.2 Profile Management

#### 3.2.1 View Profile

* Display personal info: Name, Email, Profession, Batch, Location, Bio, etc.
* Public view & private view segregation

#### 3.2.2 Edit Profile

* Alumni can update all fields
* Profile photo and cover image upload
* Visibility toggle for fields (e.g., show/hide contact info)

---

### 3.3 Events

#### 3.3.1 Create Event

* Title, description, location (physical or virtual), date, time
* Max participants
* Event banner image
* Option to enable payment (with amount)

#### 3.3.2 Event Registration

* Users can register for events
* Real-time slots availability
* Payment via Stripe, SSLCOMMERZ, or bKash (for paid events)
* View registered events

---

### 3.4 Groups

#### 3.4.1 Create Group

* Any alumni can create a group with:

    * Group name
    * Description
    * Cover image
    * Visibility (public/private)

#### 3.4.2 Group Membership

* Alumni can join/leave public groups
* Admin approval required for private groups

#### 3.4.3 Group Chat

* Real-time messaging via WebSockets
* Supports text, emoji, media (images/videos)

---

### 3.5 Chat System

#### 3.5.1 1-to-1 Chat

* Start direct chat from user profile or search
* Chat history maintained
* Typing indicator, read receipts

#### 3.5.2 Group Chat (See 3.4.3)

---

### 3.6 Audio/Video Call System

#### 3.6.1 1-to-1 Call

* Voice or video calls using WebRTC
* Call duration, mute/unmute, end call

#### 3.6.2 Group Call

* Up to X users per call
* Group chat integration
* Active speaker view

---

### 3.7 Search System

#### 3.7.1 Alumni Search

* Search by:

    * Name
    * Profession
    * Batch Year
    * Location

#### 3.7.2 Filters and Sorting

* Dropdown filters
* Sort by relevance, batch, or location

---

## 4. 📊 Non-Functional Requirements

### 4.1 Performance

* System should handle 10000 concurrent users
* Real-time updates with <1s latency

### 4.2 Security

* Encrypted passwords (bcrypt)
* HTTPS for all API calls
* OAuth token revocation

### 4.3 Scalability

* Microservices architecture for backend
* React SPA with lazy loading

### 4.4 Maintainability

* Modular codebase
* Swagger API docs

### 4.5 Usability

* Mobile responsive UI
* Intuitive navigation and UX

---

## 5. 🎨 UI/UX Expectations

### 5.1 Design Preferences

* Modern, professional design
* Color theme matching IIT Jahangirnagar branding
* Clean layout, minimal clutter

### 5.2 Pages/Components

* Home (Landing)
* Sign In / Sign Up
* Profile
* Event Page
* Group Page
* Chat Interface
* Audio/Video Call Interface
* Search Result Page
* Admin Dashboard

---

## 6. 📦 Tech Stack Recommendation

| Layer           | Technology                 |
| --------------- | -------------------------- |
| Frontend        | React + Tailwind           |
| State Mgmt      | Redux or Zustand           |
| Real-time       | Socket.IO or WebSocket API |
| Backend         | Spring Boot + JWT          |
| DB              | PostgreSQL or MySQL        |
| Media Storage   | AWS S3 / Firebase          |
| Auth            | Firebase Auth / OAuth2     |
| Payment Gateway | Stripe / bKash             |
| Video Calling   | WebRTC + PeerJS            |
| Deployment      | Docker + Kubernetes        |

---

## 7. 🔒 Future Enhancements

* Alumni donation & fundraising
* Admin analytics dashboard
* Email newsletter and notification center
* Alumni verification via university records

---

