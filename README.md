Performance Testing with Gatling
============================================

## Overview
This project is written in Java. <br>
This project uses Gatling to perform various types of performance tests on an API. 
The simulations include ```load testing```, ```stress testing```, ```spike testing```, and ```soak testing```. 
The tests are designed to evaluate the system's performance under different conditions and provide insights into its behavior.

## Project Configuration
### Maven Configuration
The Maven configuration for this project is specified in the ```pom.xml``` file.

#### Key Components
- Gatling Version: 3.11.5
- Gatling Maven Plugin Version: 4.9.6
- Jackson Databind Version: 2.17.2
- Maven Compiler Plugin Version: 3.13.0
- Maven Resources Plugin Version: 3.3.1
- Maven Jar Plugin Version: 3.4.2

### Simulations
- __Load Test__
  - _Objective_: Simulate a steady, realistic load on the system.
  - _Simulation File_: ```LoadTestSimulation.java``` 
  - _Configuration_: ```scn.injectOpen(rampUsers(50).during(10 * 60))  // Ramp up to 50 users over 10 minutes```

- ```Stress Test```
  - _Objective_: Push the system to its limits by gradually increasing the load.
  - _Simulation File_: ```StressTestSimulation.java```
  - _Configuration_: ```scn.injectOpen(rampUsers(200).during(10 * 60))  // Ramp up to 200 users over 10 minutes```

- ```Spike Test```
  - _Objective_: Simulate a sudden increase in load to test how the system handles abrupt changes.
  - _Simulation File_: ```SpikeTestSimulation.java```
  - _Configuration_: ```scn.injectOpen(atOnceUsers(100))  // Simulate a sudden spike of 100 users at once```

- ```Soak Test```
  - _Objective_: Apply a constant load over an extended period to evaluate system endurance and stability.
  - _Simulation File_: ```SoakTestSimulation.java```
  - _Configuration_: ```scn.injectOpen(rampUsers(50).during(2 * 60 * 60))  // Simulate a constant load of 50 users for 2 hours```

<span style="color:red; font-weight:bold;">[!IMPORTANT]</span><br>
- Request data file must match this json format
- Login request description __MUST__ be ```Login request```
```json
[
  {
    "feature": "Feature1",
    "requests": [
      {
        "method": "POST",
        "baseUrl": "BASE_URL",
        "endpoint": "/uri",
        "headers": {
          "Content-Type": "application/json",
          "Authorization": "Bearer token123"
        },
        "body": {
          "email": "email",
          "password": "password"
        },
        "description": "Description here",
        "expectedStatus": "Expected_Status",
        "requiresToken": false
      },
      {
        "method": "POST",
        "baseUrl": "BASE_URL",
        "endpoint": "/uri",
        "headers": {
          "Content-Type": "application/json",
          "Authorization": "Bearer token123"
        },
        "body": {
          "fname": "firstName",
          "lname": "lastName"
        },
        "description": "Description here",
        "expectedStatus": "Expected_Status",
        "requiresToken": true
      }
    ]
  }
]
```
You can refer to ```requests.json``` file

## Combination Approach
- _Objective_: Combine different traffic patterns for a comprehensive test.
- _Simulation File_: ```YOU_CAN_CREATE_IT.java```
- _Configuration_: ```scn.injectOpen(
  rampUsers(50).during(5 * 60),   // Ramp up to 50 users over 5 minutes
  constantUsersPerSec(50).during(10 * 60), // Maintain 50 users per second for 10 minutes
  rampUsers(0).during(2 * 60)   // Ramp down to 0 users over 2 minutes
  )```

## Running Simulations
- Ensure Maven is installed: Make sure Maven is installed and configured on your system
- Navigate to the project directory: Open a terminal or command prompt and navigate to the root directory of your Gatling project.
- Run the simulations: ```mvn clean test gatling:test```

This command will clean the project, compile the tests, and run all Gatling simulations defined in the ```src/test/java``` directory; specifically under the ```simulations``` package

## Modifying Simulations
To adjust the simulations for different scenarios:
- __Change the Number of Users__: Modify the rampUsers() or atOnceUsers() values in the simulation configuration to test different load levels. 
- __Adjust the Duration__: Change the during() duration to extend or shorten the test duration. 
- __Customize Requests__: Edit the JSON configuration file (requests.json) or the simulation code to include different endpoints, methods, headers, and payloads as needed. 
- __Update Base URL__: Modify the base URL in each simulation if testing against different environments or services.

## Example Results Analysis
For a sample result analysis:
- __Response Times__: Review the minimum, maximum, mean, and percentile response times to understand the system’s performance.
- __Load Handling__: Check the request rate and response time distribution to evaluate how the system handles different traffic loads.
- __Variability__: Analyze the standard deviation and percentile values to identify any significant variations in response times.

## Troubleshooting
- __If tests fail__: Check the error messages for specific issues. Ensure that the API endpoints and configurations are correct.
- __Performance issues__: Review the response times and consider increasing load or adjusting test parameters.

## Issue Reporting
If you encounter issues or have questions about the project, please follow these steps to report them:
- __Check Existing Issues__: Review the existing issues to see if your problem has already been reported or addressed.
- __Provide Detailed Information__:
  - _Describe the Issue_: Clearly describe the problem or question.
  - _Steps to Reproduce_: Provide detailed steps to reproduce the issue.
  - _Environment Details_: Include information about your environment, such as Maven version, Java version, and any relevant configuration details.
  - _Error Messages_: Attach any error messages or logs that are relevant to the issue.
- __Submit a Report__: Open a new issue on the project's [issue tracker](https://github.com/Elie-A/Galing-Java-Perf-Tests/issues/) and include all the relevant information.

## Conclusion
This project provides a comprehensive set of Gatling simulations to evaluate API performance under various conditions. By modifying the simulations and running different tests, you can gain valuable insights into how the system performs and identify areas for improvement.