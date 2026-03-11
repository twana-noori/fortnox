# Fortnox Rental System

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/twana-noori/fortnox.git
   cd fortnox
   ```

2. **Install Dependencies**
   Ensure you have [Maven](https://maven.apache.org/) installed. Then run:
   ```bash
   mvn install
   ```

3. **Configure Database**
   Update the `application.properties` file in `src/main/resources` with your PostgreSQL database credentials.

4. **Run the Application**
   Start the application using:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**
   The API will be available at `http://localhost:8080/api`.

## Usage Instructions

### Renting a Vehicle

To rent a vehicle, send a POST request to `/api/rentals` with the following JSON body:

```json
{
  "vehicleId": "string",
  "rentalPeriod": {
    "start": "YYYY-MM-DD",
    "end": "YYYY-MM-DD"
  },
  "vehicleType": "car|motorcycle"
}
```

### Checking Availability

To check available vehicles, send a GET request to `/api/rentals/available` with the following query parameters:

- `vehicleType`: `car` or `motorcycle`
- `date`: `YYYY-MM-DD`

### Validating Rental Period

To validate a rental period, send a POST request to `/api/validate` with the following JSON body:

```json
{
  "rentalPeriod": {
    "start": "YYYY-MM-DD",
    "end": "YYYY-MM-DD"
  },
  "vehicleType": "car|motorcycle"
}
```

## Project Structure

```
src
├── main
│   └── java
│       └── com
│           └── example
│               └── fortnox
│                   ├── controller
│                   │   ├── RentalApi.java
│                   │   └── ValidationService.java
│                   ├── repositories
│                   │   ├── CarRepository.java
│                   │   ├── MotorcycleRepository.java
│                   │   └── RentalRepository.java
│                   └── service
│                       ├── RentalService.java
│                       └── ValidationService.java
└── resources
    └── api-docs.yaml
```

## Data Model

### Entities
1. **Motorcycle**
   - Represents the motorcycle entity with attributes such as `id`, `model`, `availability`, etc.
2. **Rental**
   - Represents the rental transaction, which may include both cars and motorcycles.

### Relationships
- The `Rental` entity will have a polymorphic association with both `Car` and `Motorcycle`.

## Integration Points
- The new `MotorcycleRepository` will be integrated into the existing `RentalService` to manage motorcycle data.
- The `RentalApi` will be updated to handle requests for motorcycle rentals, leveraging the modified `RentalService`.
- Validation logic in `ValidationService` will be extended to include motorcycle-specific rules.

## Risk Assessment
- **Risk**: Potential for breaking changes in existing rental logic.
  - **Mitigation**: Thoroughly test all modifications, especially in the `RentalService` and `RentalApi`.
  
- **Risk**: Increased complexity in the rental process due to the addition of motorcycles.
  - **Mitigation**: Ensure clear documentation and maintain a consistent API design.

- **Risk**: Possible performance issues with additional database queries for motorcycles.
  - **Mitigation**: Optimize database access patterns and use efficient querying strategies.

- **Risk**: User confusion with the new motorcycle rental option.
  - **Mitigation**: Update UI/UX to clearly differentiate between car and motorcycle rentals, and provide adequate documentation.

## Success Criteria
- The motorcycle rental feature is fully functional and integrated into the existing rental system.
- All acceptance criteria are met and validated through unit and integration tests.
- Documentation is updated and reflects the new motorcycle rental capability.