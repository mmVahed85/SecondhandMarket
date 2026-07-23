# 🛒 Secondhand Market Project

<!-- 
بخش اول: معرفی پروژه و نام هر دو عضو گروه 
-->
## 1. 👥 Project Introduction & Team Members
This project is a comprehensive application for buying and selling secondhand goods. Users can post advertisements, chat with sellers in real-time, rate ads, and leave comments. It also includes a fully-featured Admin Panel for reviewing pending ads and managing users.

**Team Members:**
1. MohammadMehdi Vahed - 40431060
2. Javad Najjarian - 40431064

---

<!-- 
بخش دوم: توضیح پیش‌نیازها و نحوه اجرای Backend 
-->
## 2. ⚙️ Prerequisites & Backend Execution
To run the backend server, you will need the following installed on your system:
* **Java:** Version 17 and above
* **Database:** MySQL
* **Maven:** For dependency management

**Execution Steps:**
1. Navigate to the `backend` directory.
2. Open the `application.properties` file and configure your database connection settings (username, password, and database URL).
3. Open your terminal in the backend folder and run the following command to build and start the server:
   ```bash
   mvn spring-boot:run

<!-- 
بخش سوم: توضیح نحوه اجرای Frontend (۱ امتیاز) 
-->
## 3. 💻 Frontend Execution
The frontend of this application is developed using **JavaFX**.

**Execution Steps:**
1. Ensure the backend server is already up and running.
2. Navigate to the `frontend` directory.
3. Open your terminal in the frontend folder and run the application using Maven:
   ```bash
   mvn javafx:run

## 4. 🗄️ Data Storage Method & Test Accounts
**Storage Method:**
* **Database:** All relational data (users, advertisements, chat messages, comments, etc.) is stored persistently using a MySQL database. 
* **Media:** Uploaded images for advertisements are stored [e.g., locally in a designated `uploads` folder on the server / as Base64 strings in the database].

**Test Accounts (For Evaluation):**
You can use the following pre-configured accounts to evaluate different roles in the system:

* **Admin Account (For Admin Panel access):**
  * Username: `Manager`
  * Password: `1234`

* **Normal User 1:**
  * Username: `J.N`
  * Password: `jn`

* **Normal User 2:**
  * Username: `Amir.Z`
  * Password: `123456`
 
* **Normal User 3:**
  * Username: `Bro`
  * Password: `123456`

* **Normal User 4:**
  * Username: `mm.Vahed`
  * Password: `mahdi`

### 📸 Screenshots
*(Note: Images are located in the `screenshots` folder)*


**1. Main Page:**
![Main Page](screenshots/Main.png)

**2. Register Page:**
![Register Page](screenshots/Register.png)

**3. Login Page:**
![Login Page](screenshots/Login.png)

**4. Dashboard & Ads List:**
![Dashboard](screenshots/Dashboard.png)

**5. Create Ad:**
![Create Ad](screenshots/Create-Ad.png)

**6. Ad Details 1:**
![Ad Details 1](screenshots/Ad-details1.png)

**7. Ad Details 2:**
![Ad Details 2](screenshots/Ad-details1.png)

**8. Favorits Ads 2:**
![Favorits Ads](screenshots/Favorites-ads.png)

**9. Chat List page:**
![Chat List page](screenshots/Chat-list.png)

**10. Live Chat Room:**
![Live Chat Room](screenshots/Chat-room.png)

**11. Admin Panel (Manage Ads):**
![Admin Panel](screenshots/Admin-panel(manage ads).png)

**12. Admin Panel (Manage Users):**
![Admin Panel](screenshots/Admin-panel(manage users).png)

---

## 5. ✨ Implemented Features & Screenshots
The following core features have been fully implemented:
* **Authentication:** User registration, login, and secure session management.
* **User Dashboard:** Browse, filter, and view detailed pages of approved advertisements.
* **Ad Management:** Users can post new ads with images (requires Admin approval to become active).
* **Interactive Communication:** Real-time chat system between buyers and sellers.
* **Feedback System:** Users can leave comments and submit a 1-to-5 star rating on ads.
* **Admin Panel:** A dedicated portal for administrators to approve/reject pending ads, delete ads, and manage user accounts (block/unblock/delete).
