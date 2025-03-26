Objective
Develop a weather application that provides real-time weather information for multiple locations. The
app should be well-structured, maintainable, and handle errors gracefully.

Requirements
	• Reusability: Design UI components and business logic to be reusable.
	• Error Handling: Implement error handling for scenarios such as missing permissions, network
failures, and API errors.
	• API Integration: Use external weather APIs to fetch real-time data.
	• User Experience: Create an engaging and intuitive UI with smooth animations (e.g.,
transitions between views). Make the app responsive across different screen sizes.
	• Apply best practices for dependency injection (e.g., Dagger).
 
Preferred Technologies
While candidates are free to choose their preferred tools and frameworks, we encourage the use of:
	• RxJava for handling asynchronous operations.
	• Dagger 2 for dependency injection.
	• XML Views for UI development.
 
Features
1. Current Location Weather
	o Display basic weather information based on the user's current location.
	o Ensure location permissions are handled properly.
2. Global Weather Overview
	o Show weather details for three preselected cities of your choice.
	o The user should be able to tap on any city to view extended weather details.
3. Extended Weather Details
	o Implement a detailed weather view for each location.
	o Choose an intuitive way to display extended information (e.g., new screen, bottom sheet, or expandable card).
4. Asynchronous Data Fetching
	o Ensure that weather data is fetched asynchronously to maintain a smooth user
experience.
	o Use proper loading states and avoid blocking the UI during data retrieval API Usage

You may use any public weather API of your choice. For convenience, OpenWeatherMap is recommended:
	• By City Name
		https://api.openweathermap.org/data/2.5/weather?q=London,uk
		https://api.openweathermap.org/data/2.5/weather?q=London,ca
	• By Geographic Coordinates
		https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139 
