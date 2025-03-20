## Empty BE Project For Sourcery Academy 2025 Spring 

## Setup

### Prerequisites

- Java 21 JDK installed. [Link](https://adoptium.net/temurin/releases/?package=jdk&arch=x64&os=windows)

### Setup
**It's recommended to use built-in Intellij tools to run/build the app**

Command line operation:
- Start the App: `./gradlew bootRun`
- Run tests: `./gradlew test`
- Build the App: `./gradlew build`

---  

## **🐳 Running with Docker**

### **1️⃣ Start Containers**
To start **PostgreSQL** and **Azure Storage Emulator**, run:
```sh  
docker-compose up -d  
```  
This will start:  
✅ PostgreSQL database  
✅ Azurite (Azure Storage Emulator)

### **2️⃣ Stop Containers**
To stop the running containers:
```sh  
docker-compose down  
```  

### **3️⃣ Check Logs**
```sh  
docker-compose logs -f  
```  

---
## Static Analysis Tools
This project is configured with Checkstyle, PMD, and SpotBugs for static code analysis.

### Running Static Analysis
You can run all static analysis checks using:
```sh
./gradlew check
```
This will execute:

- Checkstyle: Ensures code style consistency.
- PMD: Detects potential bugs and bad practices.
- SpotBugs: Finds security and performance issues.

To run them individually:

- Checkstyle: `./gradlew checkstyleMain`
- PMD: `./gradlew pmdMain`
- SpotBugs: `./gradlew spotbugsMain`

## **🔒 Secret variables**

All secret keys are in team's Discord server, `#secrets` channel    