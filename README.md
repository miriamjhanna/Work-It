# Work It!
Android workout management app. Users may create a profile, add various workouts to their workout database, customize their daily workout routine, and track the number of completed sets for their daily workout. Developed with Android Studio, XML ViewBinding layout, Java, Cloud Firestore, and Firebase Authentication. Users can create an account with a First and Last name, email and password. Users may log in and out of the app, edit their first and last names, and change their password. If users try to change their password, they are required to re-authenticate by entering their original password. Users may also delete their account.

Work It has a database of user-created workouts containing the name of the workout, and the number of sets for the workout. The optional fields for workouts include: the number of reps, the amount of weight, and notes about the workout. Users may add, delete and edit workouts from this databas, and no two workouts can have the same name. 

Users can also log their exercises for the day. They can customize their workout for the day by deleting certain workouts from their daily workout. Deleting a workout from their daily workout will not remove it from ther exercises databse. When creating their daily workout, users may also click on each workout to see its details. Users click on the "Start Workout" button to start their workout. There, they can see how many sets they have remaining for each workout, and can click on a workout to decrement the number of sets remaining, and see an encouraging Toast message. 


