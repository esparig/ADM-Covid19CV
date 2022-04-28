This is an example development of an Android Application.

# Description

Application for Android devices. Specifically, the application shows the incidence of COVID-19 in the Comunitat Valenciana. The app allows users to check the status of the COVID-19 of the different municipalities.

## Basic development:
* Reading the JSON file that will have the data and display the data of COVID-19 per municipality in an Activity.
* To show the data in the Activity, we use either a RecyclerView.
* To create a layout for each of the items in the list that will display the specific information of each municipality, the CardView is used.
* Full information of any municipality in another activity
* Menu for accessing main options on each activity:
   * Open Covid GVA web from MunicipalitiesActivity.
   * Open Location map from MunicipalityDetailsActivity.
* Local report management system to the application that will be stored in an SQLite database:
   * Insert report through a Floating Button.
   * List reports in the Municipality Detail Activity (using a ListView).
   * Update report by clicking on one.
   * Delete report.
* Municipalities list is now obtained through an HTTP GET request to the open data web service of the Generalitat Valenciana.

## Optional development:
* Order the municipalities by the cumulative incidence (they are ordered alphabetically by default).
* Apply different colours of the rows if the cumulative incidence is low, medium, or high.
* Spinner (dropdown list) to select ordering options.
* Search bar to filter municipalities by name.
* Using the GPS sensor to obtain the current location and employ it to get the municipality's name to include it in the report.
* Retrieve the latest dataset from Generalitat Valenciana on starting.

## Screenshots:
![Municipality list](screenshot-covid19gva-01.jpg)
![Filtered municipality list](screenshot-covid19gva-02.jpg)
![Municipality details](screenshot-covid19gva-03.jpg)
![Report details](screenshot-covid19gva-04.jpg)