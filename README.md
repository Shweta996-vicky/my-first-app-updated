
# OrangeHRM Selenium Automation Framework

## Overview

A Java-based Selenium WebDriver and TestNG framework using the Page Object Model.

## Prerequisites

- Java JDK compatible with the selected Selenium version
- Eclipse or IntelliJ IDEA
- Selenium Java libraries and required dependency JARs
- TestNG and its required dependencies
- Chrome browser

## Project Features

- Visible Chrome browser execution
- Centralized visual delays and explicit waits
- Page Object Model
- Data-driven login tests
- Configuration and locator properties
- Employee creation and search
- Admin user creation
- Failure screenshots
- TestNG suite execution and reporting

## Setup

1. Create or import the Java project.
2. Add Selenium and TestNG JAR files to the project build path.
3. Ensure all required dependency JARs are included.
4. Configure `config.properties` with the authorized application URL and test credentials.
5. Verify all selectors in `locators.properties`.
6. Create the `screenshots` and `reports` directories.

## Execution

1. Open `testng.xml` in the IDE.
2. Select Run As > TestNG Suite.
3. Review the TestNG results.
4. Inspect screenshots in the `screenshots` directory for failed tests.

## Architecture

- `base`: Browser setup and reusable page interactions
- `pages`: Page Object Model classes
- `tests`: Test scenarios and assertions
- `utils`: Configuration, locators, screenshots, and listeners

## Security

Do not commit real credentials or secrets to version control.

## Known Limitations

The locators and workflows must be verified against the assigned OrangeHRM environment. Employee creation and Admin user creation may require valid test data and appropriate permissions.

## Execution Results

Record the actual passed, failed, and skipped counts after running the complete suite. Document unresolved failures and their root causes.
