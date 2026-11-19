# Furni: An On-Device Android Application for Furniture Catalog Browsing and Order Management

## Abstract

Mobile shopping applications require a coordinated combination of catalog presentation, account management, local state persistence, shopping-cart operations, inventory control, and order tracking. **Furni** is a native Android furniture-shopping application implemented in Kotlin that demonstrates these capabilities in an entirely on-device architecture. The application uses Jetpack Compose for its primary user interface, Jetpack Navigation for route management, Android ViewModels and Kotlin coroutines for presentation-state handling, and Room with SQLite for durable local persistence. The repository defines an entity model for users, furniture categories, products, orders, order-line items, and reviews. It also includes a database seeding process that provides five furniture categories, twelve representative products, and four sample reviews when the database is initialized with no catalog data.

Furni begins at a login route and provides registration and password-validation workflows, profile editing, logout, category and product browsing, product details, cart management, checkout, order confirmation, and order history. Checkout is implemented as an atomic Room transaction: the system calculates subtotal and shipping totals, inserts an order and its associated line items, and decrements product quantities only when sufficient stock is available. Navigation state, the active bottom navigation item, and the back stack are managed through Jetpack Navigation Compose. Local login state is retained with Android `SharedPreferences`, while catalog and transactional data remain in the Room database.

This document describes the repository as implemented rather than as a proposed system. No remote service or external API is present, and no algorithmic recommendation, payment, or delivery component is implemented. Empirical evaluation is unavailable: the committed unit and instrumentation tests are framework template tests rather than behavioral tests of authentication, database operations, inventory control, or UI workflows. Accordingly, this abstract reports implemented capabilities and architectural evidence only, and identifies testing, security, synchronization, and scalability as areas requiring further work.

**Keywords:** Android, Jetpack Compose, Kotlin, Room, SQLite, mobile e-commerce, MVVM, local data persistence.

## CCS Concepts

- **Human-centered computing → Mobile computing**
- **Software and its engineering → Software organization and properties → Software system structures**
- **Information systems → Data management systems → Database management system engines**
- **Information systems → Electronic commerce**

## 1. Introduction and Background

Android applications for retail and catalogue browsing must coordinate responsive interfaces with persistent domain data and reliable state transitions. Conventional mobile shopping workflows include account access, browsing, selection, cart editing, order creation, and later retrieval of purchase information. These concerns are not independent: for example, cart quantities must respect available stock, and order creation must ensure that order records and inventory updates remain consistent.

Furni addresses this domain as a self-contained mobile prototype. It uses modern Android libraries rather than a server-dependent implementation. Its Room database stores the principal entities, and Android ViewModels expose observable state to Jetpack Compose screens. This local-first design is suitable for demonstrating application structure and offline-capable workflows, although it does not by itself provide multi-device synchronization or a shared production e-commerce backend.

## 2. Problem Statement

The project investigates how to organize a small mobile furniture-shopping system within a native Android application. The specific implementation problem is to maintain coherent catalog, account, cart, inventory, and order data while allowing navigation between representative shopping activities. In particular, the system must:

1. represent related furniture catalogue entities and associations locally;
2. support account registration and login without a remote identity service;
3. present categories and products with category filtering and product detail views;
4. prevent cart quantities from exceeding available stock;
5. record an order and its line items consistently with inventory changes; and
6. present order information only for the currently authenticated local user.

The repository implements these concerns, but it does not address payment processing, real-time backend communication, delivery logistics, or remote authentication. Password storage and authentication are local and therefore have limitations that would need to be resolved before production use.

## 3. Proposed Approach and Methodology

Furni follows a presentation–domain–persistence separation centered on Android Jetpack components. `FurniApp` owns the navigation controller and materializes a bottom navigation bar for the home, products, cart, orders, and profile routes. `FurniNavHost` maps route patterns to Compose screens and forwards navigation callbacks. Routes with arguments, including category identifiers and order or product identifiers, are declared as typed navigation parameters.

The data layer is implemented with Room entities and Data Access Objects (DAOs). The `FurniDatabase` database contains `Category`, `Product`, `User`, `Order`, `OrderProduct`, and `Review` entities at schema version 1. Foreign-key relationships associate products with categories, orders with users, and order-line items with orders and products. The database also defines an indexed unique constraint on user email addresses.

ViewModels implement application behavior and publish `StateFlow` or `Flow` values to Compose UI components. `HomeViewModel` combines category records with the first six in-stock products. `CartViewModel` maintains an in-memory cart and clamps quantities to available database stock. `CheckoutViewModel` performs subtotal calculation and invokes a Room transaction for order placement. `AuthViewModel` validates registration inputs, inserts users, and saves the selected user identifier in `SharedPreferences`. `OrderDetailsViewModel` loads an order only when its stored user identifier matches the active local session.

Database seeding is guarded by a coroutine mutex and executes only when no products are present. The seed operation creates categories, then products associated with those categories, and finally sample reviews. It is safe to invoke on repeated launches and reports failures through Android logging.

## 4. Implementation and Technologies

The project uses Kotlin 2.2.10 and Android Gradle Plugin 9.4.1. The application has a minimum SDK of 24, a target and compile SDK level of 37, Java 11 source compatibility, and Compose build support. Its principal dependencies include AndroidX Core KTX, Lifecycle Runtime KTX, Lifecycle ViewModel Compose, Activity Compose, Compose Material 3, Navigation Compose, and extended Material icons.

The primary UI is implemented with Jetpack Compose in Kotlin. One login-related screen additionally uses a corresponding XML layout. Material 3 components, custom theme definitions, and a bottom navigation structure provide the interface. Assets for twelve furniture products are stored in `app/src/main/assets/product`, and the UI resolves these asset paths for product images.

Room 2.8.4 supplies local persistence. KSP generates Room implementation code from entity and DAO definitions. Coroutines provide asynchronous database access, and `StateFlow` supports reactive updates in UI state. The project contains JUnit and AndroidX test dependencies, including Espresso and Compose UI test support, but no repository evidence indicates execution of a domain-specific test suite.

Implemented user-facing capabilities include:

- local registration and login;
- profile viewing and editing, including account deletion;
- logout and session clearing;
- category-based and general product listings;
- product-detail views and sample reviews;
- stock-aware cart insertion and quantity editing;
- checkout with recipient and shipping-address input;
- fixed shipping-fee calculation;
- transactional order creation and inventory decrement;
- order confirmation and order-history/detail views.

The application does not contain REST clients, remote database dependencies, payment SDKs, notification services, or authentication providers. The account is consequently a local demonstration of identity and session behavior rather than a cloud-backed user service.

## 5. Architecture and System Workflow

At launch, `MainActivity` starts an asynchronous database-seeding operation and initializes the Compose content under `FurniTheme`. `FurniApp` installs the navigation bar and host. The navigation graph begins at login, with login and registration routes creating `AuthViewModel` instances connected to the Room database.

After successful authentication, navigation removes the login route from the back stack and enters the home destination. The home screen obtains categories and featured in-stock products through `StateFlow`. Selecting a category navigates to a filtered product screen; selecting a product opens the product-detail route. Adding an item to the cart is handled by the shared `CartViewModel`, ensuring that an item can exist only with a positive quantity and cannot exceed the current stock field.

Checkout calls `placeOrder` with cart items and recipient fields. The ViewModel first verifies an active local user and a non-empty cart. It then executes a transaction that writes the order, writes order-line items, and decrements stock. Any stock update yielding zero affected rows throws an exception, causing the surrounding transaction to fail. Successful placement exposes the generated order identifier for confirmation navigation.

Order history and detail routes retrieve persisted orders and line items. The detail view checks both order existence and ownership by the active user before exposing the order. Logout clears all values in the application preferences, and account deletion is routed to return the user to login.

## 6. Results and Evaluation

No empirical performance evaluation, user study, device-compatibility study, or behavioral test report is available in the repository. The committed local unit test only checks the arithmetic expression `2 + 2 == 4`. The committed instrumented test only verifies that the application package name is `com.example.furni`. Therefore, these tests do not evaluate application features, data integrity, inventory behavior, authentication, or navigation.

The repository nevertheless provides demonstrable implementation points that can serve as the basis for evaluation: the seed data creates a reproducible catalog, foreign keys define relational integrity, the checkout operation is transactional, and stock checks are implemented in both cart updates and checkout updates. The absence of executable feature tests means that their runtime correctness has not been established by the repository.

## 7. Conclusion and Future Work

Furni demonstrates an end-to-end local furniture-shopping workflow on Android. Its principal contributions are architectural and educational: it combines Jetpack Compose, Navigation Compose, ViewModels, Kotlin coroutines, Room, local session state, and a relational entity model within a navigable mobile application. The implementation models catalogue, account, cart, order, review, and stock concepts while keeping domain behavior close to the platform’s native APIs.

Several limitations should be addressed before the project can be described as production-ready. Passwords are stored directly in the local Room database and are not hashed or protected by platform credential APIs. Local registration does not provide account recovery or cryptographic authentication. The cart is in memory and is lost if the owning process or navigation lifetime is reset unexpectedly. No remote synchronization, conflict resolution, or multi-device account model is implemented. Checkout does not integrate a payment provider or real delivery-service API, and order status is effectively a default local value.

Future work should add hashed credential storage or a secure backend identity provider; persistent cart storage; repository abstractions and dependency injection; Room migration and rollback testing; and domain-specific unit, integration, and Compose UI tests. A meaningful evaluation should cover registration and login validation, stock exhaustion, transaction rollback, foreign-key behavior, order ownership, navigation state restoration, device-size responsiveness, and database and UI performance under larger catalog datasets.

## References

No external research citations are included because the repository contains no in-project bibliography and no verified study results. The implementation should be cited through its repository record and relevant official Android and Jetpack API documentation when submitted to an ACM venue:

- Android Jetpack Compose documentation.
- Android Jetpack Navigation documentation.
- AndroidX Room persistence library documentation.
- Kotlin coroutines and Flow documentation.
