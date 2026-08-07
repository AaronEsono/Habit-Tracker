# Privacy Policy for Habit Tracker

**Last updated:** August 7, 2026

This Privacy Policy has been prepared in accordance with Regulation (EU) 2016/679 of the European Parliament and of the Council (General Data Protection Regulation or GDPR), Spanish Organic Law 3/2018 (LOPDGDD), and taking into consideration other applicable international data protection regulations.

---

## 1. Information We Collect and How It Is Stored

The application stores the data voluntarily entered by the user to manage their habits: name, description, status, completion logs, and scheduled notifications.

In turn, the user is responsible for the content they enter into the application. It is recommended not to enter particularly sensitive information (such as health or financial data) that is not strictly necessary for tracking their habits.

By default, data is stored exclusively locally on your device (via Room and DataStore). Unless requested by the user, this data will not be uploaded to any server, and the developer has no access to it.

If the user creates an account and enables synchronization, the data entered into the application will be stored in **Google Firebase Firestore**, a Google Firebase service used as a data storage and synchronization provider.

You can learn more about how Google processes personal data by consulting their Privacy Policy:
* [Google Privacy Policy](https://policies.google.com/privacy)
* [Privacy and Security in Firebase](https://firebase.google.com/support/privacy)

---

## 2. Third-Party Services and Analytics

The application uses certain services provided by Google Firebase in order to offer a better user experience, ensure the proper functioning of the application, and improve its performance.

In particular, the application uses the following services:

* **Firebase Authentication:** for authentication and management of user accounts.
* **Cloud Firestore:** to store and synchronize user data when the user chooses to create an account and enable cloud synchronization.
* **Firebase Analytics:** to collect statistical and usage information from the application, allowing for the analysis of its performance and the improvement of user experience.
* **Firebase Crashlytics:** to detect, log, and analyze technical errors or unexpected application crashes in order to resolve issues and enhance stability.

The information collected by these services may include technical device data, operating system information, application version, installation identifiers, usage events, and crash logs. This data is used exclusively for the operation, maintenance, analysis, and improvement of the application.

---

## 3. Legal Basis and International Data Transfers

* **Legal Basis:** The processing of data synchronized to the cloud is based on the execution of the service requested by the user. Where required by applicable regulations, certain features may require the user's prior consent.
* **International Transfers:** Cloud synchronization through Firebase may involve data processing on Google servers located outside the European Economic Area (EEA), relying on Google's international transfer safeguards and agreements in compliance with applicable law.

---

## 4. Requested Permissions

To provide all of its features, the application will request the following permissions on your device:

* **Notifications (`POST_NOTIFICATIONS`):** Necessary to send you habit reminders at the days and times set by the user.
* **Display over other apps (`SYSTEM_ALERT_WINDOW`):** Required solely and exclusively if you use the floating timer feature (*overlay*) while using other applications.

You can revoke these permissions at any time from your operating system settings.

---

## 5. Data Retention

* **Locally stored data:** Will remain on the user's device until the user manually deletes them or uninstalls the application.
* **Cloud-synchronized data:** Will remain stored as long as the user's account remains active, or until the user requests their deletion or deletes their account.

Once the data or account is deleted, backup copies may be retained temporarily for the strictly necessary period due to technical or legal reasons, after which they will be permanently deleted.

---

## 6. Control, Data Deletion, and User Rights

### A. Managing Your Data:
* **Local Data:** You can clear all local data by uninstalling the application or clearing internal storage from your phone settings.
* **Cloud Data:** If you signed in with Google, you can request the complete deletion of your data or the deletion of your account directly from within the app, or by contacting the developer.

### B. Data Protection Rights:
As a user, you have the right to:
* Access your personal data.
* Rectify inaccurate data.
* Request the erasure of your data (Right to be forgotten).
* Request the restriction of processing of your data in cases provided by applicable law.
* Object to the processing of your personal data where appropriate.
* Request the portability of your personal data, where technically feasible and legally applicable.
* Withdraw given consent at any time, without affecting the lawfulness of processing based on consent before its withdrawal.

To exercise any of these rights, you can send an email to the developer at the address provided at the end of this document.

---

## 7. Children's Privacy

The application is not directed toward minors under the minimum age required by applicable law to validly consent to the processing of personal data. We do not knowingly collect personal information from minors. If it is detected that cloud data of a minor has been stored without parental/legal guardian consent, it will be promptly deleted.

---

## 8. Information Security

Reasonable technical and organizational measures are implemented to protect personal data against unauthorized access, loss, modification, or disclosure. However, no method of electronic transmission or storage can guarantee absolute security.

---

## 9. Changes to This Policy

We reserve the right to update this Privacy Policy if we add new features. We recommend reviewing this page periodically to stay informed of any changes.

---

## 10. Contact

If you have any questions or concerns about this privacy policy, you can contact the developer using the following information:
* **Data Controller:** Aaron Esono
* **Email:** habittrackerappae@gmail.com
* **Country:** Spain

The application offers a feature to report issues using the user's email client. In that case, the message draft may include technical information such as the app version, device model, time zone, or date and time, solely for the purpose of facilitating issue resolution. The user can review, modify, or remove this information prior to sending the email.