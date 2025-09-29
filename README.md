# ⚖️ Distributed Load Balancer System  

A **custom load balancer** built using **Java (Spring Boot)** and a **React.js + Vite + Tailwind CSS dashboard**, supporting multiple load balancing algorithms, real-time server health monitoring, and traffic visualization.  

---

## ✨ Features  

- 🔄 **Load Balancing Algorithms**  
  - Round Robin  
  - Least Connections  
  - Consistent Hashing (with Virtual Nodes)  
  - Weighted Round Robin  

- 🖥️ **Server Health Monitoring**  
  - Active / Inactive status tracking  
  - Connection count monitoring  
  - Auto failover if server is unhealthy  

- 📊 **Interactive Dashboard** (React + Tailwind + Chart.js)  
  - Home page with overall status  
  - **Server Condition Page** → real-time health & load info  
  - **Algorithm Selection Page** → choose strategy & send test requests  
  - Bar charts to visualize request distribution across servers  

- 🧪 **Mock HTTPS Servers** for local simulation & testing  

---
## ⚙️ Tech Stack

### Backend
- Java, Spring Boot (WebFlux / WebClient optional)  
- Lombok, SLF4J / Logback  
- Custom load balancing strategies (Strategy pattern)  

### Frontend
- React (Vite)  
- Plain CSS (or Tailwind option)  
- Recharts / Chart.js for charts (optional)  

### Dev / Testing
- Node.js (for mock servers)  
- OpenSSL (for self-signed certs if using HTTPS mocks)  
- Docker / docker-compose (optional)  

---

