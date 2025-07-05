# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

### Build Project
```bash
mvn clean install
```

### Build without Tests
```bash
mvn clean install -DskipTests=true
```

### Run Applications
```bash
# Temperature Loader
mvn exec:java -Dexec.mainClass=org.infinispan.tutorial.client.temperature.TemperatureLoaderApp

# Temperature Monitor
mvn exec:java -Dexec.mainClass=org.infinispan.tutorial.client.temperature.TemperatureMonitorApp

# Weather Loader
mvn exec:java -Dexec.mainClass=org.infinispan.tutorial.client.weather.WeatherLoaderApp

# Weather Finder
mvn exec:java -Dexec.mainClass=org.infinispan.tutorial.client.weather.WeatherFinderApp

# Health Checker
mvn exec:java -Dexec.mainClass=org.infinispan.tutorial.client.HealthChecker
```

### Run Tests
```bash
mvn test
```

## Project Architecture

This is an Infinispan Server tutorial demonstrating remote Hot Rod client connections. The project is built around two main subsystems:

### Temperature Subsystem
- **TemperatureLoader**: Stores temperature data (String location -> Float temperature) in a `temperature` cache with 20-second expiration
- **TemperatureMonitor**: Uses client listeners to monitor temperature changes and display notifications

### Weather Subsystem  
- **WeatherLoader**: Stores complex weather data (String location -> LocationWeather POJO) in a `weather` cache
- **WeatherSearch**: Performs queries including FROM queries, SELECT queries, and continuous queries on weather data

### Core Components

#### Data Layer
- `DataSourceConnector`: Manages Remote Cache Manager connections to Infinispan Server
- `LocationWeather`: Record class for weather data (temperature, condition, city, country)
- `LocationWeatherMarshallingContext`: Handles Protobuf serialization for complex objects

#### Application Layer
- `App`: Abstract base class for all client applications
- Client applications in `org.infinispan.tutorial.client.*` packages demonstrate different features

#### Services Layer
- `TemperatureLoader`: Implements caching with expiration for temperature data
- `TemperatureMonitor`: Demonstrates client listeners for cache events
- `WeatherSearch`: Shows querying capabilities (FROM, SELECT, continuous queries)
- `FullWeatherLoader`: Loads weather data into queryable cache

### Key Technical Details

#### Cache Configuration
- Temperature cache uses simple String->Float mapping with Protobuf encoding
- Weather cache stores complex POJOs with full-text search capabilities
- Both caches configured via XML files in src/main/resources/

#### Serialization
- Uses Protostream for Protobuf serialization
- LocationWeather requires @Proto annotations and schema generation
- Build process generates protobuf schemas at compile time

#### Connection Setup
- Default connection: `hotrod://admin:password@localhost:11222`
- Requires running Infinispan Server instance
- Authentication credentials: admin/password (configurable)

## Development Notes

### Prerequisites
- JDK 17+
- Running Infinispan Server instance
- Docker/Podman for integration tests

### Important Build Steps
1. Always run `mvn clean install` before executing main classes
2. Protobuf schemas are generated at build time
3. Use `-DskipTests=true` if experiencing Docker-related test issues

### Testing
- Uses Testcontainers with JUnit 5 for integration tests
- `InfinispanServerExtension` provides test server instances
- Docker required for running tests