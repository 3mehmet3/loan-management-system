# 💳 Loan Management System

<p align="center">
  <img src="https://img.shields.io/badge/Java-11+-orange?style=for-the-badge&logo=openjdk" />
  <img src="https://img.shields.io/badge/SQLite-3.45-blue?style=for-the-badge&logo=sqlite" />
  <img src="https://img.shields.io/badge/FlatLaf-3.4-green?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maven-3.8+-red?style=for-the-badge&logo=apachemaven" />
  <img src="https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge" />
</p>

A **Java Swing** desktop application for managing bank loan applications with role-based access control. Built as a learning project to demonstrate OOP principles, database integration, and multi-user system design.

---

## ✨ Features

### 👤 Staff (Personel)
- Submit loan applications with customer details
- Select loan type, term, and interest rate
- Save applications to the database

### 🏢 Branch Manager (Şube Müdürü)
- View pending loan applications
- Approve or reject applications
- View staff directory

### ⭐ General Manager (Genel Müdür)
- Monitor all loan applications and approval statuses
- View full staff and manager directories
- System-wide oversight

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 11+ | Core language |
| Java Swing | - | Desktop UI framework |
| SQLite | 3.45 | Local database |
| FlatLaf | 3.4.1 | Modern UI theme |
| Maven | 3.8+ | Dependency management |

---

## 🚀 Getting Started

### Prerequisites
- Java 11 or higher
- Maven 3.8 or higher

### Run the application

```bash
# Clone the repository
git clone https://github.com/3mehmet3/loan-management-system.git
cd loan-management-system

# Build and run
mvn clean install
mvn exec:java -Dexec.mainClass="com.loanmanagement.WelcomePage"
```

Or run the JAR directly:
```bash
java -jar target/loan-management-system.jar
```

---

## 🔐 Test Credentials

| Role | Username | Password |
|------|----------|----------|
| Staff | `sema` | `2823` |
| Staff | `mehmet` | `4226` |
| Branch Manager | `ademoglu` | `4721` |
| Branch Manager | `bturhan` | `8834` |
| General Manager | `aydinoglu` | `5342` |

---

## 📁 Project Structure

```
loan-management-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/loanmanagement/
│       │       ├── WelcomePage.java           # Application entry point
│       │       ├── LoginPage.java             # Unified login screen (all roles)
│       │       ├── PersonelDashboard.java     # Staff panel - loan applications
│       │       ├── MudurDashboard.java        # Manager panel - approvals
│       │       ├── GenelMudurDashboard.java   # GM panel - full oversight
│       │       ├── DatabaseManager.java       # SQLite database layer (Singleton)
│       │       └── UITheme.java              # Centralized UI theme & components
│       └── resources/
│           └── images/
├── pom.xml
└── README.md
```

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────┐
│              UI Layer                   │
│  WelcomePage → LoginPage → Dashboard   │
├─────────────────────────────────────────┤
│           Business Logic                │
│         DatabaseManager                 │
├─────────────────────────────────────────┤
│           Data Layer                    │
│         SQLite (loan_system.db)         │
└─────────────────────────────────────────┘
```

**Design Patterns used:**
- **Singleton** — DatabaseManager
- **MVC** — UI separated from data logic
- **Factory** — UITheme component factory

---

## 📸 Screenshots

> *Coming soon*

---

## 🗺️ Roadmap

- [x] Role-based login system
- [x] Loan application submission
- [x] Manager approval workflow
- [x] SQLite database integration
- [x] Modern UI with FlatLaf
- [ ] REST API version (Spring Boot) — *in progress*
- [ ] Unit tests (JUnit 5)
- [ ] Docker support

---

## 👨‍💻 Author

**Mehmet Karakaş**
- GitHub: [@3mehmet3](https://github.com/3mehmet3)
- LinkedIn: [https://www.linkedin.com/in/mehmet-karakaş-a743b4267/](https://linkedin.com)

---

## 📄 License

This project is licensed under the MIT License.
