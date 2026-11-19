# Furni: Design and Implementation of a Local-First Android Furniture Shopping Application

## Abstract

Mobile commerce applications require the integration of product catalog management, user accounts, shopping-cart operations, inventory tracking, and order processing within a consistent user experience. **Furni** is a native Android furniture-shopping application developed in Kotlin that demonstrates these capabilities through a local-first architecture. The application employs Jetpack Compose for its user interface, Navigation Compose for screen transitions, Android ViewModels and Kotlin coroutines for application-state management, and Room with SQLite for persistent data storage.

The system provides user registration and login, profile management, category-based product browsing, product details, shopping-cart management, checkout, and order history. Its data model represents users, furniture categories, products, orders, order-line items, and reviews. A database initialization process populates the application with five furniture categories, twelve sample products, and four reviews when the catalog is initially empty. A central aspect of the implementation is transactional checkout: the application calculates order totals, creates an order and its associated line items, and updates product inventory within a Room database transaction. Stock availability is checked before quantities are updated, helping maintain consistency between orders and inventory.

Furni demonstrates how modern Android development components can be combined to implement a local shopping workflow without requiring a remote backend. However, the current implementation does not provide remote authentication, payment processing, delivery-service integration, or multi-device synchronization. Furthermore, the available repository tests do not evaluate the application's principal business workflows. This work therefore presents an implementation-focused account of the system and identifies security, testing, persistence, and scalability improvements needed for further development.

**Keywords:** Android Development, Mobile Commerce, Kotlin, Jetpack Compose, Room Database, SQLite, MVVM, Inventory Management.

## CCS Concepts

- **Human-centered computing → Mobile computing**
- **Software and its engineering → Software architectures**
- **Information systems → Database management systems**
- **Information systems → Electronic commerce**

## 1. Introduction and Problem Statement

Mobile shopping applications must coordinate multiple interconnected operations, including account management, product discovery, cart updates, inventory control, and order processing. Maintaining consistency between these operations is particularly important when an order changes product quantities or when users navigate between different application screens.

Furni addresses these requirements through the development of a native Android furniture-shopping application. The project focuses on implementing a structured shopping workflow using platform-native technologies while maintaining application data locally. Its primary objective is to demonstrate how catalog information, user accounts, shopping-cart state, and order records can be integrated into a single mobile application.

Unlike conventional cloud-based commerce systems, Furni uses an entirely on-device data architecture. This approach provides a self-contained environment for demonstrating mobile application development and local data management. However, it also introduces limitations concerning remote access, account security, and synchronization across devices.

## 2. System Design and Methodology

Furni follows a Model-View-ViewModel (MVVM)-oriented architecture that separates the user interface, application logic, and data persistence responsibilities.

The presentation layer is implemented using Jetpack Compose and Material 3 components. Navigation Compose manages transitions between authentication, home, product listing, product details, shopping cart, checkout, profile, and order-related screens. ViewModels manage application operations and expose observable data through Kotlin Flow and StateFlow, allowing the interface to react to changes in application state.

The persistence layer uses Room, an Android abstraction over SQLite, to manage relational application data. The database consists of six principal entities: User, Category, Product, Order, OrderProduct, and Review. Foreign-key relationships associate products with categories, orders with users, and order-line records with their corresponding orders and products. A unique database constraint is also defined for user email addresses.

Database initialization is performed through a seeding mechanism that inserts sample categories, products, and reviews when the catalog is empty. This provides a reproducible dataset for demonstrating the application's main functionality.

Application behavior is distributed across dedicated ViewModels. Authentication operations manage registration, login, profile updates, and local session state. Product-related screens retrieve catalog information and support category filtering. The shopping-cart functionality maintains selected products and validates requested quantities against available inventory.

The checkout process represents an important part of the system methodology. When a user submits an order, the application calculates the subtotal and shipping cost, verifies the active local user, and performs the order creation process within a database transaction. The transaction inserts the order and associated order-line records before decrementing product quantities. If a stock update fails, an exception is raised so that the database transaction can be rolled back. This design aims to preserve consistency between order records and inventory data.

## 3. Implementation and Technologies

The application is implemented in Kotlin using modern Android development technologies. Jetpack Compose provides the primary interface, while Navigation Compose manages application navigation. Android ViewModels and Kotlin coroutines support asynchronous operations and presentation-state management. Room and SQLite provide persistent storage for catalog, account, review, and order information.

The application includes local registration and login, profile management, category-based product browsing, product-detail views, sample reviews, stock-aware cart operations, checkout, order confirmation, and order history. Product images are maintained within the application's assets directory. Local session information is retained through Android SharedPreferences.

The application does not depend on an external REST API or remote database. Authentication and account information are handled locally, and order records are stored on the device. Consequently, the implementation demonstrates local shopping functionality rather than a complete production e-commerce infrastructure.

## 4. Results and Evaluation

The repository demonstrates an implemented shopping workflow covering the main stages of a furniture purchase, from account access and product browsing to checkout and order retrieval. Its relational database structure defines the associations between users, products, categories, and orders, while transactional checkout provides a mechanism for coordinating order creation and inventory updates.

However, no comprehensive empirical evaluation or application-specific behavioral testing is available in the repository. The existing unit test checks a basic arithmetic expression, while the instrumentation test verifies the application package name. Neither test evaluates authentication, database integrity, inventory management, transaction rollback, navigation, or order processing.

Therefore, the project's results are limited to its implemented functionality and architectural design. Performance, usability, reliability, and scalability have not yet been established through systematic experiments.

## 5. Conclusion and Future Work

Furni demonstrates the design and implementation of a local-first Android furniture-shopping application using Kotlin and modern Android development frameworks. Its main contribution is the integration of catalog browsing, account management, cart operations, inventory updates, and order processing within a structured native mobile architecture.

Despite implementing the principal shopping workflow, several limitations remain. User credentials are stored locally without password hashing, the shopping cart is maintained in memory, and the application does not provide remote authentication, payment processing, delivery integration, or cross-device synchronization.

Future development should focus on secure credential management, persistent shopping-cart storage, backend integration, and comprehensive automated testing. Additional evaluation should examine database consistency, checkout reliability, application responsiveness, navigation behavior, and performance with larger product catalogs.

## References

[1] Google. Android Developers. *Jetpack Compose Documentation*. https://developer.android.com/compose

[2] Google. Android Developers. *Room Persistence Library*. https://developer.android.com/training/data-storage/room

[3] Google. Android Developers. *Navigation for Jetpack Compose*. https://developer.android.com/develop/ui/compose/navigation

[4] JetBrains. *Kotlin Documentation*. https://kotlinlang.org/docs/home.html

[5] JetBrains. *Kotlin Coroutines Documentation*. https://kotlinlang.org/docs/coroutines-overview.html
