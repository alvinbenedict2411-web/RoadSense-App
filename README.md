# 🛣️ RoadSense — Real-Time AI Pothole Detection & Smart Road Reporting

> **Making roads safer, one pothole at a time.**  
> An Android application that uses on-device artificial intelligence to detect potholes through both live camera feeds and selected images, record their locations, and synchronize reports to the cloud for road maintenance monitoring.

---

## 📌 About the Project

Potholes are one of the common causes of road accidents, vehicle damage, and uncomfortable driving conditions. However, identifying and reporting damaged roads is still largely dependent on manual inspections and individual complaints.

These methods can be slow, inconsistent, and difficult to monitor. Road authorities may not receive accurate information about where potholes exist, how severe they are, or whether previously reported potholes have been repaired.

**RoadSense aims to make road monitoring smarter through real-time computer vision and location-based reporting.**

Instead of requiring users to manually report every pothole, RoadSense uses a smartphone camera and a custom-trained AI model to detect potholes automatically.

The application provides two ways to detect potholes:

1. **Live Camera Detection:** Continuously analyses the live camera feed and identifies potholes using an on-device YOLOv8n model.
2. **Static Image Detection:** Allows users to select an existing image from their device and analyse it using the same custom-trained AI model.

Both detection modes generate pothole detection results that can be recorded locally, viewed in reports, and synchronized with Firebase.

The application is built around three main stages:

1. **Detect:** Analyse live camera frames or selected images and identify potholes using the on-device YOLOv8n model.
2. **Record:** Capture pothole information, including its location, image, confidence score, and estimated size, and save it locally.
3. **Synchronize and Monitor:** Upload recorded reports to Firebase, maintain their repair status, and make the information available for cloud-based monitoring.

RoadSense uses Firebase for cloud functionality. **Each developer who clones this repository must configure their own Firebase project.** The project does not provide access to the original developer's Firebase database or billing account.

The larger goal is to connect everyday road users with the departments responsible for maintaining public roads.

---

# ⭐ What Makes RoadSense Different?

## 1. ⚡ Real-Time On-Device AI Detection

The standout feature of RoadSense is its ability to detect potholes directly from a live camera feed.

Rather than requiring users to capture individual photographs and upload them for analysis, the application continuously processes camera frames and identifies potholes while the camera is running.

- Uses a custom-trained YOLOv8n object detection model.
- Model exported to TensorFlow Lite for Android deployment.
- Runs inference locally on the smartphone.
- Displays detection bounding boxes over the live camera preview.
- Processes frames without uploading the continuous camera feed to a cloud server.
- Supports detection without requiring an active internet connection.

This makes RoadSense more than a conventional image-based detection application. It works as a mobile, AI-powered road-monitoring system.

## 2. 🖼️ AI-Based Static Image Detection

RoadSense also supports pothole detection through images selected from the device gallery.

Users do not always need to run the live camera to detect potholes. They can select an existing road image and analyse it using the same custom-trained YOLOv8n model.

The selected image goes through the same core AI detection process used by live detection:

- Image loading and preprocessing.
- Resizing and pixel conversion.
- On-device YOLOv8n inference.
- Model output decoding.
- Confidence-based filtering.
- Non-Maximum Suppression (NMS).
- Bounding-box coordinate processing.
- Detection result rendering.

The application displays the detected potholes and their corresponding information.

### What happens after static image detection?

Static image detection is not limited to displaying bounding boxes temporarily.

Its results can also become pothole reports.

The application can record information such as:

- Detection image.
- Bounding-box information.
- Detection confidence.
- Estimated pothole size.
- Available geographical coordinates.
- Area and city information.
- Detection timestamp.

The generated records are stored in the local Room Database and can be accessed through the application's reports section. They can also be synchronized with Firebase.

**In short, live camera detection and static image detection use the same AI detection engine. The main difference is the source of the image being analysed. Both contribute to the application's pothole reporting system.**

## 3. 🧠 Advanced Live Camera Processing

Real-time object detection on Android involves considerably more than simply loading a machine learning model.

The application must capture camera frames, prepare them for inference, execute the model, interpret its output, and display the resulting detections in the correct position on the screen.

RoadSense handles this through a dedicated camera and inference pipeline involving CameraX and the custom YOLOv8n model.

The pipeline includes:

- Continuous camera frame acquisition.
- Image conversion, rotation, and resizing.
- Pixel preprocessing and model input preparation.
- TensorFlow Lite inference.
- YOLO output decoding.
- Confidence-based filtering.
- Non-Maximum Suppression (NMS).
- Bounding-box coordinate transformation.
- Live detection overlay rendering.

These operations must work together efficiently to provide a responsive camera experience.

## 4. ☁️ Cloud Synchronization and Pothole Status Monitoring

A pothole report should not remain useful only on the smartphone that detected it.

This is why RoadSense includes Firebase cloud synchronization.

When a user detects potholes through either live camera detection or static image detection, the information can be stored locally and synchronized with Firebase.

RoadSense also maintains a status for cloud pothole reports:

- **Pending:** The pothole has been reported and is awaiting completion of road maintenance.
- **Completed:** The pothole repair has been marked as completed.

Authorities can update the status of an uploaded report through an authorized cloud-side mechanism. The updated status can then be viewed in the RoadSense application's Cloud Reports and Cloud Details screens.

This makes cloud synchronization more than simply storing detection information. It also provides a way to monitor the progress of reported road problems.

The cloud reporting system provides a foundation for:

- Viewing reported potholes across different locations.
- Identifying roads and areas with frequent pothole reports.
- Viewing uploaded images and detection information.
- Monitoring whether reports are pending or completed.
- Keeping the application updated with cloud report status changes.
- Supporting road maintenance monitoring.

## 5. 🗺️ Location-Based Pothole Mapping

Every recorded pothole can be associated with geographical information.

RoadSense uses location services to collect coordinates and Android Geocoder to obtain available area and city information.

The application uses this information to:

- Display pothole locations on Google Maps.
- Identify the area where a pothole was detected.
- View recorded pothole details.
- Help users identify previously reported road hazards.
- Provide a foundation for geographical analysis of road conditions.

## 6. 💾 Offline-First Detection and Local Storage

RoadSense uses Room Database to store pothole records locally.

This is important because network connectivity may not always be available while travelling.

Both live detection and static image detection can perform their AI processing locally without requiring Firebase connectivity.

Detection results can be saved on the device until the user synchronizes them.

This separates the detection process from the cloud reporting process.

---

# 🔬 Deep Dive: How AI Pothole Detection Works

The AI detection system is the most technically significant part of RoadSense.

The application combines Android camera functionality, image processing, and a custom-trained object detection model to identify potholes.

RoadSense supports two detection modes:

- **Live Detection:** Processes a continuous stream of camera frames.
- **Static Image Detection:** Processes an image selected by the user.

Both modes use the same underlying YOLOv8n model and core detection-processing operations.

## Detection Architecture

### Live Camera Detection

```text
             SMARTPHONE CAMERA
                    |
                    v
          +--------------------+
          |      CameraX       |
          |   Live Camera Feed |
          +--------------------+
                    |
                    v
          +--------------------+
          |   ImageAnalysis    |
          |  Frame Acquisition |
          +--------------------+
                    |
                    v
          +--------------------+
          | Image Processing   |
          | Rotation / Resize  |
          | Pixel Conversion   |
          +--------------------+
                    |
                    v
          +--------------------+
          |  YOLOv8n Model     |
          | TensorFlow Lite    |
          |   On-Device AI     |
          +--------------------+
                    |
                    v
          +--------------------+
          | Output Decoding    |
          | Confidence Filter  |
          |      NMS           |
          +--------------------+
                    |
                    v
          +--------------------+
          | Bounding Box       |
          | Coordinate Mapping |
          +--------------------+
                    |
                    v
          +--------------------+
          | Live Camera Overlay|
          | Detection Results  |
          +--------------------+
                    |
                    v
          +--------------------+
          | Location & Details |
          | Pothole Information|
          +--------------------+
                    |
                    v
          +--------------------+
          |   Room Database    |
          |   Local Storage    |
          +--------------------+
```

### Static Image Detection

```text
             DEVICE GALLERY
                    |
                    v
          +--------------------+
          |   Image Selection  |
          |   Selected Image   |
          +--------------------+
                    |
                    v
          +--------------------+
          | Image Preprocessing|
          | Resize / Convert   |
          | Pixel Preparation  |
          +--------------------+
                    |
                    v
          +--------------------+
          |  YOLOv8n Model     |
          | TensorFlow Lite    |
          |   On-Device AI     |
          +--------------------+
                    |
                    v
          +--------------------+
          | Output Decoding    |
          | Confidence Filter  |
          |      NMS           |
          +--------------------+
                    |
                    v
          +--------------------+
          | Bounding Box       |
          | Coordinate Mapping |
          +--------------------+
                    |
                    v
          +--------------------+
          | Detection Results  |
          | Annotated Image    |
          +--------------------+
                    |
                    v
          +--------------------+
          | Location & Details |
          | Pothole Information|
          +--------------------+
                    |
                    v
          +--------------------+
          |   Room Database    |
          |   Local Storage    |
          +--------------------+
```

## Step-by-Step Technical Workflow

### 1. Live Frame Capture — CameraX

RoadSense uses Android CameraX to access the smartphone camera.

Instead of capturing only one photograph, the application receives a continuous stream of camera frames.

The ImageAnalysis use case provides frames for processing by the detection system.

Using a latest-frame processing strategy helps avoid building up a queue of old frames when inference takes longer than the camera's frame interval.

This is important because processing outdated frames would make the detection overlay appear delayed compared with the actual camera feed.

### 2. Static Image Selection

For static image detection, RoadSense allows users to select an existing image from their device.

The selected image is passed to the same AI detection engine used by live camera detection.

Unlike live detection, there is no continuous frame acquisition. The application processes the selected image and generates detection results for that particular image.

This makes it possible to analyse previously captured road photographs without running the camera.

### 3. Image Preprocessing

The YOLO model expects input in a specific format and dimensions.

However, camera frames and gallery images can have different resolutions, orientations, and aspect ratios depending on the device and image source.

Before inference, the application prepares the incoming image.

This involves:

- Converting image data into a format suitable for model processing.
- Handling image rotation information where required.
- Resizing the image to the model's expected input dimensions.
- Converting image pixels into the required RGB representation.
- Preparing the input buffer for TensorFlow Lite.

Preprocessing is essential because incorrectly formatted images can produce inaccurate or unusable model predictions.

### 4. On-Device YOLOv8n Inference

RoadSense uses a custom-trained YOLOv8n model exported as a TensorFlow Lite model.

The model file is:

```text
app/src/main/assets/pothole_model.tflite
```

The model analyses the processed image and predicts possible pothole locations.

Both live camera frames and selected gallery images are processed using this model directly on the Android device.

Unlike a cloud-based detection approach, inference takes place locally.

This provides several advantages:

- No need to upload every camera frame or selected image for AI analysis.
- Reduced dependence on internet connectivity.
- Lower network usage during detection.
- Faster response compared with sending images to a remote server.
- Better privacy because continuous camera footage remains on the device.

Only the relevant pothole report information is intended to be synchronized with Firebase.

### 5. YOLO Output Decoding and Confidence Filtering

The model produces detection predictions rather than directly providing Android screen coordinates.

The application must interpret these predictions to identify valid pothole detections.

The processing includes:

1. Reading the model output tensor.
2. Decoding predicted bounding-box coordinates.
3. Extracting confidence information.
4. Filtering predictions using a confidence threshold.
5. Preparing the remaining detections for further processing.

Confidence filtering helps remove predictions that are not sufficiently reliable.

### 6. Non-Maximum Suppression (NMS)

A single pothole may generate multiple overlapping bounding-box predictions.

Displaying every prediction would create unnecessary overlapping boxes.

RoadSense uses Non-Maximum Suppression to reduce duplicate overlapping detections.

NMS uses Intersection over Union (IoU) to measure the overlap between bounding boxes.

It generally works by:

1. Selecting the prediction with the highest confidence.
2. Comparing it with other predicted boxes.
3. Calculating their IoU.
4. Suppressing sufficiently overlapping lower-confidence predictions.
5. Retaining the remaining detections.

This produces cleaner and more meaningful detection results.

### 7. Mapping Detection Coordinates to the Image or Live Preview

One of the challenging parts of object detection is displaying bounding boxes in the correct position.

The model processes an image in its own coordinate system, while the Android camera preview and selected image display use their own dimensions.

These coordinate systems are not necessarily identical.

RoadSense therefore needs to account for image scaling, rotation, preview dimensions, and camera orientation when transforming detection coordinates.

For live detection, the resulting bounding boxes are drawn over the camera preview.

For static detection, the resulting bounding boxes are displayed in relation to the selected image.

This allows users to see exactly where the model identified potholes in either detection mode.

### 8. Location Collection and Local Record Creation

When a pothole is detected and accepted for recording, RoadSense collects the relevant information.

The record can contain:

- Latitude.
- Longitude.
- Area name.
- City name.
- Detection confidence.
- Estimated pothole size.
- Captured or selected image.
- Detection timestamp.

The application uses Android location services to obtain geographical coordinates and Geocoder to obtain available location names.

The bounding-box dimensions are also used by the application to estimate pothole size.

Finally, the detection information is stored in the local Room Database.

This creates a persistent pothole report that can be viewed, managed, and synchronized later.

**An important point:** Both live and static detection contribute to the same reporting system. Static detection is not just a temporary image-analysis feature; its saved results can also become part of the user's pothole reports.

---

# ☁️ Cloud Synchronization: From Driver Reports to Road Authorities

## Why Does RoadSense Need Cloud Synchronization?

Imagine that ten different drivers travel through the same city.

Each driver encounters potholes on different roads and detects them using RoadSense.

Some drivers may use live camera detection, while others may analyse previously captured road images using static image detection.

If all the reports remain inside their respective smartphones, there is no central way to understand the overall condition of the city's roads.

This is the problem cloud synchronization is designed to address.

RoadSense uses Firebase to move locally recorded pothole information into a cloud database associated with the authenticated user.

The cloud reporting system also supports status updates, allowing reports to be identified as Pending or Completed.

## Cloud Synchronization Workflow

```text
       DRIVER 1                 DRIVER 2
    +------------+           +------------+
    | RoadSense  |           | RoadSense  |
    |   App      |           |   App      |
    +------------+           +------------+
          |                         |
          v                         v
    +------------+           +------------+
    | Live /     |           | Live /     |
    | Static AI  |           | Static AI  |
    | Detection  |           | Detection  |
    +------------+           +------------+
          |                         |
          v                         v
    +------------+           +------------+
    | Room DB    |           | Room DB    |
    | Local Data |           | Local Data |
    +------------+           +------------+
          |                         |
          +------------+------------+
                       |
                       v
              User Initiates Sync
                       |
                       v
             +---------------------+
             | Developer's Own     |
             | Firebase Project    |
             | Realtime Database   |
             | Firebase Storage    |
             +---------------------+
                       |
                       v
             +---------------------+
             | Cloud Report Status |
             | Pending / Completed |
             +---------------------+
                       |
                       v
             +---------------------+
             | RoadSense Cloud     |
             | Reports Screen      |
             +---------------------+
                       |
                       v
              View Updated Status
```

## How Synchronization Works

### 1. Local Report Creation

When a pothole is detected through live camera detection or static image detection, the application stores its information in the Room Database.

This allows reports to remain available even without an internet connection.

### 2. User Authentication

RoadSense uses Firebase Authentication to identify the signed-in user.

Each user's pothole reports are stored under their own Firebase user ID.

### 3. Firebase Realtime Database

When synchronization takes place, the application uploads pothole information to Firebase Realtime Database.

The database structure follows this pattern:

```text
users/
  {userUID}/
    potholes/
      {potholeFirebaseKey}/
        localId
        firebaseKey
        latitude
        longitude
        areaName
        cityName
        confidence
        potholeSize
        imageUrl
        status
```

The `status` field represents the current maintenance status of the report.

Its values are:

| Status | Meaning |
|---|---|
| Pending | The reported pothole is awaiting repair completion. |
| Completed | The pothole repair has been marked as completed. |

The exact record fields are determined by the application implementation.

### 4. Firebase Storage

Pothole images are uploaded separately to Firebase Storage.

The image's download URL is associated with the corresponding pothole record in Realtime Database.

This means the cloud database can store structured information while Firebase Storage holds the actual image files.

### 5. Cloud Status Updates

RoadSense supports maintenance status tracking for cloud pothole reports.

After a report has been uploaded, its status can be updated in the cloud by an authorized party managing the report.

For example:

1. A user detects a pothole using live or static image detection.
2. The pothole report is saved locally and synchronized with Firebase.
3. The uploaded report initially has a Pending status.
4. An authorized party updates the report's status in the cloud after the repair is completed.
5. The status becomes Completed.
6. The updated status can be viewed in the RoadSense application's Cloud Reports and Cloud Details screens.

This provides visibility into the progress of pothole reports instead of simply storing detection records.

**Important:** The Android application implements cloud synchronization and displays the report status. A dedicated municipal web dashboard and centralized authority portal remain future integration possibilities. Authority access must be implemented with appropriate authentication and security permissions.

---

# 📱 Application Features

## 1. 🏠 Home Dashboard

The home screen provides an overview of the user's pothole detection activity.

Features include:

- Detection activity information.
- Total potholes detected.
- Recent detection details.
- Quick navigation to live detection.
- Access to image-based detection.
- Access to the pothole map.

## 2. 📹 Live Camera Detection

The central feature of RoadSense.

- Continuous camera frame processing.
- On-device YOLOv8n inference.
- Real-time pothole detection.
- Bounding-box overlays.
- Detection confidence display.
- Camera switching support.

## 3. 🖼️ Static Image Detection

RoadSense also allows users to detect potholes from existing images.

Unlike a separate image-processing feature, static detection uses the same underlying AI detection engine as live camera detection.

Features include:

- Select images from the device gallery.
- Run the custom-trained YOLOv8n model on the selected image.
- Perform image preprocessing and model inference locally.
- Decode predictions and filter detections.
- Apply Non-Maximum Suppression.
- Display bounding boxes on the selected image.
- View detection confidence and estimated pothole sizes.
- Save detection results as pothole reports.

The resulting reports are stored in Room Database and can be viewed alongside reports generated through live camera detection.

This provides flexibility: users can either detect potholes while travelling or analyse road images they have already captured.

## 4. 🗺️ Google Maps

The map screen provides a geographical view of recorded potholes.

Features include:

- Displaying pothole locations.
- Viewing available pothole details.
- Identifying the area and city.
- Differentiating markers by pothole size.
- Accessing information about individual reports.
- Displaying available local and cloud records.

## 5. 🗃️ Local Pothole Reports

The local reports screen displays potholes stored in Room Database.

Reports can originate from either live camera detection or static image detection.

Users can:

- View previously detected potholes.
- Inspect detection information.
- View saved images.
- Filter available records.
- Delete local reports.

Both detection modes contribute to the same local reporting system, making it possible to manage all recorded potholes in one place.

## 6. ☁️ Cloud Reports and Synchronization

The cloud section provides access to the application's Firebase synchronization functionality.

Features include:

- Viewing cloud-synchronized reports.
- Initiating report synchronization.
- Associating reports with the authenticated Firebase account.
- Accessing information stored in the cloud.
- Viewing the current status of individual pothole reports.
- Identifying reports marked as Pending or Completed.
- Viewing status updates made to the corresponding cloud records.

The Cloud Details screen displays information such as the pothole image, location, size, confidence, coordinates, detection date, and maintenance status.

This allows users to see whether a reported pothole is still pending or has been marked as completed.

## 7. 👤 User Profile

The profile section provides user account information and account-related functionality.

It includes Firebase authentication integration and sign-out functionality.

---

# 🛠️ Technology Stack

| Category | Technology |
|---|---|
| Programming Language | Kotlin |
| UI Framework | Jetpack Compose |
| UI Components | Material 3 |
| Camera | Android CameraX |
| Artificial Intelligence | Custom YOLOv8n |
| Model Format | TensorFlow Lite |
| On-Device Inference | LiteRT |
| Local Database | Room Database |
| Cloud Database | Firebase Realtime Database |
| Cloud Image Storage | Firebase Storage |
| Authentication | Firebase Authentication |
| Google Sign-In | Credential Manager |
| Maps | Google Maps Compose |
| Location | Fused Location Provider |
| Image Loading | Coil 3 |
| Asynchronous Operations | Kotlin Coroutines and Flow |

---

# 🏗️ Application Architecture

RoadSense separates the application into data, domain, and presentation-related components.

```text
                    RoadSense
                        |
          +-------------+-------------+
          |             |             |
          v             v             v
       DATA          DOMAIN      PRESENTATION
          |             |             |
     +----+----+        |       +-----+------+
     |         |        |       |            |
     v         v        v       v            v
   Room    Firebase  Repository Screens   ViewModels
 Database  Services  Contracts
     |         |
     +----+----+
          |
          v
    Repository Layer
          |
          v
    Application Data
```

The presentation layer handles the user interface and application state.

The domain layer defines repository contracts, while the data layer handles local database operations and Firebase communication.

This separation helps keep application logic organized and makes individual components easier to maintain.

---

# 📂 Project Structure

```text
RoadSense/
│
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── assets/
│   │       │   └── pothole_model.tflite
│   │       │
│   │       ├── java/com/example/beastmode_5/
│   │       │   ├── App/
│   │       │   │   ├── Data/
│   │       │   │   │   ├── FireBase/
│   │       │   │   │   ├── Reposistory/
│   │       │   │   │   └── RoomDataBase/
│   │       │   │   │
│   │       │   │   ├── Domain/
│   │       │   │   │
│   │       │   │   └── Presentation/
│   │       │   │       ├── Classes/
│   │       │   │       ├── Navigation/
│   │       │   │       ├── Screens/
│   │       │   │       └── Viewmodels/
│   │       │   │
│   │       │   └── MainActivity.kt
│   │       │
│   │       ├── res/
│   │       └── AndroidManifest.xml
│   │
│   └── build.gradle.kts
│
├── build.gradle.kts
├── gradle/
├── gradlew
├── gradlew.bat
└── README.md
```

---

# 🚀 Getting Started

Follow these steps to set up RoadSense locally.

## Prerequisites

- Android Studio.
- Android device or emulator.
- Android SDK compatible with the project configuration.
- A Firebase account.
- A Google Cloud account for Maps API configuration.

For testing the live detection feature, a physical Android device with a working camera is recommended.

## 1. Clone the Repository

```bash
git clone https://github.com/alvinbenedict2411-web/RoadSense.git

cd RoadSense
```

## 2. Open the Project

1. Open Android Studio.
2. Select **Open**.
3. Navigate to the cloned RoadSense folder.
4. Allow Gradle to synchronize the project.

## 3. Configure Your Own Firebase Project

**Important: RoadSense does not include the original developer's Firebase configuration. Each developer must create and configure their own Firebase project to use the cloud features.**

Follow these steps:

### Step 1: Create a Firebase Project

1. Open the [Firebase Console](https://console.firebase.google.com/).
2. Sign in using your Google account.
3. Select **Create a project**.
4. Enter a project name.
5. Complete the Firebase project setup.

### Step 2: Register the Android Application

1. Open your newly created Firebase project.
2. Select **Add app** and choose Android.
3. Enter the Android package name:

```text
com.example.beastmode_5
```

4. Register the application.
5. Download the generated `google-services.json` file.

### Step 3: Add Firebase Configuration

Place your downloaded configuration file inside the application's `app` directory:

```text
RoadSense/
└── app/
    └── google-services.json
```

Do not place this file in the project root.

The application uses the Firebase configuration provided by this file to connect to your own Firebase project.

### Step 4: Enable Firebase Authentication

1. Open **Authentication** in the Firebase Console.
2. Select **Get started** if required.
3. Open the **Sign-in method** section.
4. Enable the authentication providers used by RoadSense.

If you use Google Sign-In:

- Enable the Google provider.
- Configure the required SHA-1 certificate fingerprint.
- Ensure the registered Android application matches the package name.

### Step 5: Create Firebase Realtime Database

1. Open **Realtime Database** in the Firebase Console.
2. Select **Create Database**.
3. Choose an appropriate database location.
4. Complete the database creation.
5. Configure the database security rules.

RoadSense uses Firebase Realtime Database to store pothole report information.

The database URL is obtained through the Firebase configuration rather than requiring you to use the original developer's database URL.

### Step 6: Configure Firebase Storage

1. Open **Storage** in the Firebase Console.
2. Set up a Firebase Storage bucket.
3. Configure the required Storage security rules.

RoadSense uses Firebase Storage to store pothole images.

**Note:** Firebase Storage may require the Blaze billing plan depending on the current Firebase project requirements. Check the applicable pricing and billing conditions before enabling it.

### Step 7: Configure Realtime Database Rules

The following example rules restrict authenticated users to accessing their own pothole records:

```json
{
  "rules": {
    "users": {
      "$uid": {
        "potholes": {
          ".read": "auth != null && auth.uid === $uid",
          ".write": "auth != null && auth.uid === $uid"
        }
      }
    }
  }
}
```

These rules provide user-specific access to pothole reports.

**Important:** These are user-scoped rules. They do not provide municipal authorities with access to every user's reports. A separate, securely designed authorization mechanism is required for centralized authority access and report management.

## 4. Configure Google Maps

RoadSense requires a Google Maps API key.

1. Open [Google Cloud Console](https://console.cloud.google.com/).
2. Create or select a Google Cloud project.
3. Enable the Maps SDK for Android.
4. Configure the required APIs for the project's location and geocoding functionality.
5. Generate an API key.
6. Create a file named `secrets.properties` in the project root.

Add:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

The project's Secrets Gradle Plugin reads this configuration and makes the key available to the Android application.

Restrict the API key to the appropriate Android application and required APIs.

## 5. Configure Google Sign-In

If using Google Sign-In:

- Enable the Google provider in Firebase Authentication.
- Configure the appropriate SHA certificate fingerprints.
- Ensure the Firebase Android application configuration matches the project package name.

## 6. Run the Application

1. Connect an Android device.
2. Enable USB debugging.
3. Open the project in Android Studio.
4. Complete Gradle synchronization.
5. Run the application.

Grant the required camera and location permissions when prompted.

---

# 🔐 Security and Configuration

The following configuration files are intentionally excluded from the GitHub repository:

```text
google-services.json
secrets.properties
local.properties
```

Each developer must provide their own configuration when cloning the project.

Important security considerations:

- Do not commit private service-account credentials.
- Restrict Google Maps API keys.
- Configure Firebase Authentication correctly.
- Use appropriate Firebase database security rules.
- Implement separate authorization for municipal authority access.
- Consider Firebase App Check to help protect backend resources.
- Monitor Firebase usage and billing if using a paid plan.

**Firebase configuration note:** The Firebase Android configuration file contains project identifiers and client configuration. It is not a substitute for security rules. Keep your database and Storage rules properly configured.

**Firebase Storage note:** Image synchronization requires a configured Firebase Storage bucket. Firebase Storage may require the Blaze billing plan depending on the current Firebase project requirements. Check the applicable pricing and billing conditions before enabling it.

---

# 🎯 Project Vision

RoadSense is built around a simple idea:

**What if every smartphone could become a tool for identifying damaged roads and helping authorities respond to them?**

By combining real-time on-device AI, static image analysis, camera processing, location intelligence, local data storage, and cloud synchronization, RoadSense aims to reduce the gap between detecting a road problem and reporting it to the people responsible for fixing it.

The live detection system makes identifying potholes while travelling easier, while static image detection allows users to analyse previously captured road photographs.

Both detection methods contribute to a common reporting system, ensuring that detected potholes can be stored, reviewed, and synchronized rather than remaining temporary AI predictions.

Cloud synchronization and Pending/Completed status tracking provide visibility into reported road problems and their maintenance progress.

The ultimate goal is not simply to detect potholes, but to make that information useful for improving road infrastructure.

---

**Built with Kotlin, Jetpack Compose, CameraX, YOLOv8n, LiteRT, and Firebase.**

🚧 **RoadSense — Making roads safer, one pothole at a time.**