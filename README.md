# 💱 Currency Exchange Counter Management System

A Java-based **Currency Exchange Counter Management System** developed to manage currency exchange operations, customer details, exchange rates, and transaction records through an interactive desktop application.

The project demonstrates the practical use of **Object-Oriented Programming (OOP), Java Collections, Swing GUI, Exception Handling, Input Validation, and Event Handling**.

---

## 📌 Project Overview

The **Currency Exchange Counter Management System** is designed to simulate the operations of a currency exchange counter.

The system allows users to:

* Manage different currencies
* Maintain exchange rates
* Enter customer details
* Calculate currency conversions
* Create and manage exchange transactions
* Validate user input
* Handle invalid or incorrect data
* Display transaction information through a graphical user interface

The main purpose of this project is to demonstrate how Java concepts can be combined to develop a practical desktop-based application.

---

## 🎯 Objectives

The main objectives of this project are:

1. To develop a functional currency exchange management application using Java.
2. To implement Object-Oriented Programming principles.
3. To manage currencies and exchange rates efficiently.
4. To maintain customer and transaction information.
5. To use Java Collections for storing and managing data.
6. To create an interactive GUI using Java Swing.
7. To implement input validation and exception handling.
8. To provide a simple and user-friendly interface for currency exchange operations.

---

## ✨ Features

### 💱 Currency Management

* Add and manage different currencies.
* Store currency codes and names.
* Maintain exchange rates.
* Support currency conversion.

### 📊 Exchange Rate Management

* Store exchange rates for different currencies.
* Retrieve exchange rates when performing conversions.
* Calculate converted amounts based on the selected currencies.

### 👤 Customer Management

* Store customer information.
* Associate customers with transactions.
* Validate customer input.

### 💳 Transaction Management

* Create currency exchange transactions.
* Store transaction details.
* Calculate exchanged amounts.
* Track transaction information.

### 🖥️ Graphical User Interface

The application uses **Java Swing** to provide an interactive desktop interface.

The GUI contains components such as:

* `JFrame`
* `JPanel`
* `JLabel`
* `JTextField`
* `JComboBox`
* `JButton`
* `JTable`
* Dialog boxes

### ✅ Input Validation

The system validates user input to prevent invalid data such as:

* Empty fields
* Invalid currency selection
* Invalid numeric values
* Negative amounts
* Incorrect customer information

### ⚠️ Exception Handling

Exception handling is used to prevent the application from crashing when invalid input or unexpected situations occur.

---

# 🛠️ Technologies Used

| Technology                 | Purpose                      |
| -------------------------- | ---------------------------- |
| Java                       | Main programming language    |
| Java Swing                 | Graphical User Interface     |
| Java Collections Framework | Data management              |
| ArrayList                  | Store collections of objects |
| LinkedList                 | Manage sequential data       |
| HashMap                    | Store key-value data         |
| TreeMap                    | Store sorted key-value data  |
| Exception Handling         | Handle runtime/input errors  |
| OOP                        | Application architecture     |

---

# 🧠 Java Concepts Used

## 1. Object-Oriented Programming

The project follows the major principles of OOP:

### Encapsulation

Data members are kept inside classes and accessed through appropriate methods.

### Inheritance

Classes can inherit common properties and behaviours from parent classes where required.

### Polymorphism

Methods can behave differently depending on the object or implementation being used.

### Abstraction

The implementation details are separated from the functionality exposed to the user.

---

## 2. Classes Used

The project is divided into different classes based on their responsibilities.

### Currency

Represents a currency used by the exchange system.

Example information:

* Currency name
* Currency code
* Exchange rate

### ExchangeRate

Manages exchange rate information between currencies.

### Transaction

Represents a currency exchange transaction.

It stores information such as:

* Transaction ID
* Customer
* Source currency
* Target currency
* Amount
* Converted amount

### Customer

Represents a customer performing a currency exchange.

---

# 📦 Java Collections Used

The project demonstrates different Java Collection Framework classes.

## ArrayList

`ArrayList` is used when an ordered collection of objects needs to be maintained.

Example:

```java
ArrayList<Transaction> transactions = new ArrayList<>();
```

It allows multiple transaction objects to be stored dynamically.

---

## LinkedList

`LinkedList` can be used for maintaining data where frequent insertion and deletion operations may be required.

Example:

```java
LinkedList<Customer> customers = new LinkedList<>();
```

---

## HashMap

`HashMap` stores data in **key-value pairs**.

Example:

```java
HashMap<String, Double> exchangeRates = new HashMap<>();
```

The currency code can be used as the key and the exchange rate as the value.

Example:

```text
USD → 83.20
EUR → 90.50
GBP → 105.30
```

---

## TreeMap

`TreeMap` stores key-value pairs in sorted order.

Example:

```java
TreeMap<String, Double> sortedRates = new TreeMap<>();
```

This can be useful when currency information needs to be displayed in sorted order.

---

# 🖥️ GUI Design

The application uses **Java Swing** for creating the graphical user interface.

Important Swing components used include:

### JFrame

Creates the main application window.

### JPanel

Organizes GUI components into sections.

### JLabel

Displays text and labels.

### JTextField

Allows the user to enter information.

### JComboBox

Allows the user to select a currency.

### JButton

Performs actions such as:

* Convert
* Add
* Update
* Delete
* Clear
* Submit

### JTable

Displays structured transaction or customer information.

### JOptionPane

Displays messages, warnings, errors, and confirmation dialogs.

---

# 🔄 How the System Works

The basic workflow of the system is:

```text
Start Application
       ↓
Open Currency Exchange System
       ↓
Enter Customer Details
       ↓
Select Source Currency
       ↓
Select Target Currency
       ↓
Enter Amount
       ↓
Validate Input
       ↓
Retrieve Exchange Rate
       ↓
Calculate Converted Amount
       ↓
Create Transaction
       ↓
Display Transaction Result
```

---

# 💰 Currency Conversion

The basic currency conversion process is:

```text
Converted Amount = Amount × Exchange Rate
```

For example, if:

```text
Amount = 100 USD
Exchange Rate = 83.20
```

Then:

```text
Converted Amount = 100 × 83.20
                 = 8320 INR
```

The actual exchange rate used by the application depends on the stored exchange-rate data.

---

# 🔐 Validation and Exception Handling

Input validation is implemented to ensure that the application receives valid information.

Examples of validation include:

* Checking whether required fields are empty.
* Checking whether an amount is numeric.
* Checking whether the amount is positive.
* Checking whether valid currencies are selected.
* Preventing invalid transactions.

Exception handling can be implemented using:

```java
try {
    // code that may produce an exception
} catch (Exception e) {
    // handle exception
}
```

This helps the application handle errors without unexpectedly terminating.

---

# 📁 Project Structure

A typical project structure is:

```text
Currency-Exchange-Counter-Management-System/
│
├── src/
│   ├── Currency.java
│   ├── ExchangeRate.java
│   ├── Transaction.java
│   ├── Customer.java
│   └── Main.java
│
├── screenshots/
│   └── currency-exchange-system.png
│
├── README.md
└── .gitignore
```

> The exact file names and folders may vary depending on the final project structure.

---

# 🚀 How to Run the Project

## Prerequisites

Before running the project, make sure you have:

* Java Development Kit (JDK)
* IntelliJ IDEA / Eclipse / VS Code
* Git (optional, for GitHub)

---

## Method 1: Run Using IntelliJ IDEA

### Step 1

Open **IntelliJ IDEA**.

### Step 2

Select:

```text
File → Open
```

and select the project folder.

### Step 3

Wait for IntelliJ IDEA to load and index the project.

### Step 4

Open the Java file containing the `main()` method.

For example:

```java
public static void main(String[] args)
```

### Step 5

Click the green **Run ▶** button.

The application will start and the GUI will open.

---

# 🧪 Example Transaction

A sample transaction can look like:

```text
Customer: John
From Currency: USD
To Currency: INR
Amount: 100

Exchange Rate: 83.20

Converted Amount: 8320 INR
```

The transaction can then be displayed in the transaction table.

---


## 🏠 Application Interface

<img width="1470" height="956" alt="Screenshot 2026-10-01 at 7 26 54 PM" src="https://github.com/user-attachments/assets/5a9f0193-ae60-4b1c-9922-971bc1195b1c" />


```markdown
![Currency Exchange System]
```


---

## 💱 Currency Conversion Screen

<img width="1470" height="956" alt="Screenshot 2026-10-01 at 7 27 29 PM" src="https://github.com/user-attachments/assets/95a88f16-45c6-40c6-8959-d28125146f98" />
<img width="1470" height="956" alt="Screenshot 2026-10-01 at 7 27 45 PM" src="https://github.com/user-attachments/assets/ee5daff1-60ba-4a31-b1b5-81da82826d0c" />


```markdown
![Currency Conversion]
```

---

## 📊 Transaction Records

<img width="1470" height="956" alt="Screenshot 2026-10-01 at 7 28 24 PM" src="https://github.com/user-attachments/assets/8ba04e08-ce6e-4f28-b601-261091af186b" />


```markdown
![Transaction Records]
```



---

# 🎨 User Interface

The application provides a simple desktop-based interface where users can enter information and perform currency exchange operations.

The interface is designed to make the following operations easy to access:

* Customer entry
* Currency selection
* Amount entry
* Currency conversion
* Transaction management
* Viewing transaction records

---

# 🧩 Main Modules

The project can be divided into the following modules:

```text
1. Customer Management
        ↓
2. Currency Management
        ↓
3. Exchange Rate Management
        ↓
4. Currency Conversion
        ↓
5. Transaction Management
        ↓
6. GUI
        ↓
7. Validation & Exception Handling
```

---

# 📚 Concepts Demonstrated

This project demonstrates the following Java concepts:

* Classes and Objects
* Constructors
* Encapsulation
* Inheritance
* Polymorphism
* Abstraction
* Methods
* Access Modifiers
* Static Members
* Java Collections
* ArrayList
* LinkedList
* HashMap
* TreeMap
* Exception Handling
* Input Validation
* Java Swing
* Event Handling
* GUI Components
* JTable
* JOptionPane
* Layout Management

---

# 🎓 Learning Outcomes

After completing this project, the following concepts were practically implemented:

* Designing a Java application using OOP.
* Creating multiple classes with specific responsibilities.
* Using collections to store application data.
* Creating a GUI using Java Swing.
* Handling user-generated events.
* Validating user input.
* Handling exceptions.
* Performing currency conversion calculations.
* Managing transaction records.

---

# 🔮 Future Enhancements

The project can be extended with additional features such as:

* Database connectivity using MySQL/PostgreSQL.
* User login and authentication.
* Admin dashboard.
* Real-time exchange rates using an API.
* Transaction history search.
* Transaction export to PDF/CSV.
* Customer account management.
* Receipt generation.
* Improved UI/UX.
* Multi-user support.
* Cloud-based data storage.

---

# 👩‍💻 Author

**Akshara Tanted**

B.Tech Computer Science & Engineering

ITM Skills University

---

# 🔗 Links

### GitHub

[GitHub Profile](https://github.com/aksharatanted0676-alt)

### LinkedIn

[LinkedIn Profile](https://www.linkedin.com/in/akshara-tanted-3aaab2387/)

---

# 📄 License

This project was developed for educational and academic purposes.

You are free to use the project for learning and reference.

---

# ⭐ Project Summary

The **Currency Exchange Counter Management System** is a Java desktop application that demonstrates how core Java programming concepts can be applied to develop a practical real-world system.

The project combines:

```text
Java
+
OOP
+
Collections
+
Swing GUI
+
Event Handling
+
Validation
+
Exception Handling
+
Currency Conversion
+
Transaction Management
```

to create a functional currency exchange management application.
