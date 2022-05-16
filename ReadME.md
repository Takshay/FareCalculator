#Fare Calculator

#Assumption
    * PAN as unique identifier for Taps
    * Trips file directory exist.

###Steps :
    * Checkout repository
    * Navigate to project directory
    * Clean and build `./gradlew clean build`
    * Run `./gradlew run --args="src/main/resources/taps.csv src/main/resources/trips.csv"`