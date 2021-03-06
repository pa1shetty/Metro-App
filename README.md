# Metro App
This is a practice project which is trying to build an app for metro user 
With following functionalities
  * User can Register
  * Allows the user to add, see, recharge their metro card
  * User can purchase QR ticket or pass and use it in metro station without human interaction.
 The app is built in such a way that it can be used for any transportation with minimum tweaks and configuration changes.
 
 The goal of this project is to practice the all-new skills which I have learned, build an app with clean code, and follow good practices suggested by google.
 And build an app with good-looking and user-friendly UI/UX.
 
 ##  Features
 - [x] Splash screen
    - [x] App update
    - [x] Download configuration
    - [x] Check for registration
    
 - [x] Login Screen
    - [x] Login
    - [x] Auto fetch user phone number
    - [x] Auto fetch OTP

 - [x] Settings screen
    - [x] Logout
    - [x] Theme change
    - [ ] Language change

- [x] Home Screen
- [ ] Card functionality
- [x] Qr Ticket functionality
   - [x]  Qr Ticket list Screen
   - [x]  Ticket Details Screen
   - [x]  Ticket Purchase screen
   - [x]  Creating QR Code from data.
   - [ ]  Payment Gateway integration.

Around 70% of the development has been completed.
    
## 📸 Video
#### Login HomeSreen Settings 

https://user-images.githubusercontent.com/24766565/156736631-83ae3d66-f7cb-4c2a-9bb0-13568c701b45.mp4



#### Qr Ticket Screen

https://user-images.githubusercontent.com/24766565/161376126-36591198-8325-43bc-bf64-652406a0bf13.mp4



## 📸 Screenshots

||||
|:----------------------------------------:|:-----------------------------------------:|:-----------------------------------------: |
| ![Splash Screen](media/1.jpg) | ![Login Screen](media/2.jpg) | ![Otp Screen](media/3.jpg) |
| ![Home Screen](media/4.jpg)  | ![Settings Screen - Light](media/5.jpg) | ![Home Screen - Light](media/6.jpg)    |
| ![Settings Screen - Light](media/7.jpg) | ![Qr Ticket - Unused](media/9.jpg) | ![Qr Ticket - Other](media/10.jpg) |
| ![Qr Ticket - Unused - Light](media/11.jpg) | ![Qr Ticket - Other - Light](media/12.jpg) | ![Qr Ticket Purchase Screen - Station Selection](media/13.jpg) |
| ![Qr Ticket Purchase Screen](media/14.jpg) | ![Qr Ticket Purchase Screen - Travel Date](media/15.jpg) | ![Qr Ticket Purchase Screen - Confirmation](media/16.jpg) |
| ![Qr Ticket Purchase Screen - Light](media/17.jpg) | ![Qr Ticket Details Screen ](media/18.jpg) | ![Qr Ticket Details Screen - Light](media/19.jpg) |



## Built With 🛠
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) -
  - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
  - [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting `ViewModel`.
- Backend
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [GSON](https://github.com/google/gson) - A modern JSON library for Kotlin and Java.
- [GSON Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson) - A Converter that uses Moshi for serialization to and from JSON.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Navigation Component](https://developer.android.com/guide/navigation) - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
- [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) - LiveData is an observable data holder class. Unlike a regular observable, LiveData is lifecycle-aware, meaning it respects the lifecycle of other app components, such as activities, fragments, or services. 
- [SMS User Consent API](https://developers.google.com/identity/sms-retriever/user-consent/overview) -The SMS User Consent API complements the SMS Retriever API by allowing an app to prompt the user to grant access to the content of a single SMS message. When a user gives consent, the app will then have access to the entire message body to automatically complete SMS verification.


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

![](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

Single Activity Architecture

## Contact 📩

[![Mail](https://img.shields.io/badge/Gmail-green.svg?style=for-the-badge&logo=gmail)](mailto://spavanm1@gmail.com)

[![LinkedIn](https://img.shields.io/badge/LinkedIn-red.svg?style=for-the-badge&logo=linkedin)](https://www.linkedin.com/in/pavan-m-shetty-79211068/)


<br>
