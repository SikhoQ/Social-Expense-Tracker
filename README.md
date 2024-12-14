---

# **WeShare: A Social Expense Tracker**  

WeShare is a web application that helps users track shared expenses with friends. Built on the principle of social trust, it simplifies managing expenses, payment requests, and repayments in a collaborative and transparent way.  

---

## **Features**  

### **1. User Management**  
- Users can log in using a valid email address.  
- No password or token-based authentication is required for simplicity.  

### **2. Expense Tracking**  
- Record and categorize personal expenses with details like:  
  - **What:** Description of the expense.  
  - **When:** Date of the expense.  
  - **How much:** Amount spent.  
- View a breakdown of your expenses.  

### **3. Payment Requests**  
- Log shared expenses and send payment requests to friends.  
- Record how much each friend owes for a specific shared expense.  
- Track payment requests sent to and received from friends.  

### **4. Payments**  
- Once a payment request is fulfilled:  
  - The payer records the payment as an expense.  
  - The original sender's expense is reduced by the paid amount.  
- View outstanding payments:  
  - **People who owe you**  
  - **People you owe**  

---

## **User Journeys**  

The application includes several user journeys that outline the key interactions within the app:  
1. **Adding an Expense:** Log what you spent, when you spent it, and the amount.  
2. **Creating Payment Requests:** Split shared expenses with friends by sending payment requests.  
3. **Tracking Payments:** Keep track of who owes you and whom you owe.  
4. **Recording Payments:** Update the expense and payment records when a request is fulfilled.  

> **Note:** Refer to the provided `WeShareUserJourneys.pdf` file for detailed mockups of user flows. The CSS files included are basic and serve as placeholders for the mockups. Feel free to customize your page layouts and styles for a better user experience.  

---

## **Technologies Used**  

- **Frontend:**  
  - HTML, CSS, JavaScript (for creating interactive user interfaces).  

- **Backend:**  
  - Java with Javalin framework for handling HTTP requests and routing.  

- **Database:**  
  - SQLite for persistent storage of user, expense, and payment data.  

- **Templating:**  
  - Thymeleaf for server-side rendering of dynamic content.  

---

## **How to Run the Project**  

1. **Clone the Repository:**  
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```  

2. **Install Dependencies:**  
   Ensure you have Java (JDK 17 or later) installed, then run:  
   ```bash
   ./mvnw clean install
   ```  

3. **Start the Application:**  
   ```bash
   ./mvnw exec:java
   ```  

4. **Access the Application:**  
   Open your web browser and navigate to `http://localhost:7000`.  

---

## **Project Structure**  

```
├── Makefile
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── weshare
    │   │       ├── controller
    │   │       │   ├── ExpensesController.java
    │   │       │   └── PersonController.java
    │   │       ├── model
    │   │       │   ├── DateHelper.java
    │   │       │   ├── Expense.java
    │   │       │   ├── MoneyHelper.java
    │   │       │   ├── Payment.java
    │   │       │   ├── PaymentRequest.java
    │   │       │   ├── Person.java
    │   │       │   └── WeShareException.java
    │   │       ├── persistence
    │   │       │   ├── collectionbased
    │   │       │   │   ├── ExpenseDAOImpl.java
    │   │       │   │   └── PersonDAOImpl.java
    │   │       │   ├── ExpenseDAO.java
    │   │       │   └── PersonDAO.java
    │   │       └── server
    │   │           ├── Routes.java
    │   │           ├── ServiceRegistry.java
    │   │           └── WeShareServer.java
    │   └── resources
    │       ├── html
    │       │   ├── css
    │       │   │   ├── main.css
    │       │   │   ├── normalize.css
    │       │   │   └── styles.css
    │       │   └── index.html
    │       └── templates
    │           ├── exception.html
    │           ├── expenses.html
    │           ├── layout.html
    │           ├── newexpense.html
    │           ├── paymentrequest.html
    │           ├── paymentrequests_received.html
    │           └── paymentrequests_sent.html
    └── test
        └── java
            └── weshare
                ├── model
                │   ├── ExpenseTests.java
                │   ├── PaymentRequestTests.java
                │   ├── PaymentTests.java
                │   └── PersonTests.java
                ├── persistence
                │   └── collectionbased
                │       ├── ExpenseDAOTests.java
                │       └── PersonDAOTests.java
                ├── server
                │   ├── ServiceRegistryTests.java
                │   └── WeShareServerTests.java
                └── webtests
                    ├── ExpenseFormTests.java
                    ├── ExpensesPageTests.java
                    ├── LoginAndLogoutTests.java
                    ├── pages
                    │   ├── AbstractPage.java
                    │   ├── ExpenseForm.java
                    │   ├── ExpensesPage.java
                    │   ├── LoginPage.java
                    │   ├── PaymentRequestForm.java
                    │   ├── PaymentRequestsReceivedPage.java
                    │   └── PaymentRequestsSentPage.java
                    ├── PaymentRequestFormTests.java
                    ├── PaymentRequestsReceivedPageTests.java
                    ├── PaymentRequestsSentPageTests.java
                    ├── WebSession.java
                    └── WebTestRunner.java

```

---

## **Key Learning Outcomes**  

This project consolidates concepts in:  
1. **Web Development:**  
   - Building dynamic, server-rendered web applications with Javalin and Thymeleaf.  
2. **Database Design and SQL:**  
   - Creating and querying relational database schemas for expense tracking and payment management.  
3. **HTTP APIs:**  
   - Implementing RESTful interactions between the frontend and backend.  
4. **Collaboration:**  
   - Iterative development and problem-solving through teamwork.  

---

## **Future Enhancements**  
- Add user authentication for better security.  
- Implement a dashboard with visual expense summaries (e.g., charts).  
- Add support for multi-currency transactions.  
- Integrate with payment gateways for real-time payment handling.  

---

## **Contributors**  

This project was completed as part of the WeThinkCode_ program.  

--- 
