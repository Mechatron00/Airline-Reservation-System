classDiagram
direction BT
class AirlineSystemApplication {
  + AirlineSystemApplication() 
  + main(String[]) void
}
class AirlineSystemApplicationTests {
  ~ AirlineSystemApplicationTests() 
  ~ contextLoads() void
}
class BadRequest {
  + BadRequest() 
  + BadRequest(int, String) 
  - String message
  - int code
  + equals(Object) boolean
  # canEqual(Object) boolean
  + hashCode() int
  + toString() String
   int code
   String message
}
class ExceptionHandle {
  + ExceptionHandle(BadRequest) 
  - BadRequest badRequest
  + toString() String
  + hashCode() int
  + equals(Object) boolean
  # canEqual(Object) boolean
   BadRequest badRequest
}
class Flight {
  + Flight() 
  + Flight(String, String, String, int, String, double, Date, Date, Plane, List~Passenger~) 
  - String flightNumber
  - List~Passenger~ passengers
  - Plane plane
  - Date departureTime
  - Date arrivalTime
  - String fromLocation
  - String destination
  - int seatLeft
  - String description
  - double price
  + equals(Object) boolean
  + hashCode() int
   String description
   String flightNumber
   List~Passenger~ passengers
   double price
   Date departureTime
   Plane plane
   Date arrivalTime
   int seatLeft
   String fromLocation
   String destination
}
class FlightController {
  + FlightController() 
  + deleteFlight(String) ResponseEntity~?~
  + updateFlight(String, double, String, String, String, String, String, int, String, String, String) ResponseEntity~?~
  + getFlightByNumber(String) ResponseEntity~?~
}
class FlightRepository {
<<Interface>>
  + getFlightByFlightNumber(String) Optional~Flight~
}
class FlightService {
  + FlightService() 
  + deleteFlight(String) void
  - checkValidUpdate(Flight, Date, Date) boolean
  + updateFlight(String, double, String, String, String, String, String, int, String, String, String) ResponseEntity~?~
  + getFlightByNumber(String) ResponseEntity~?~
}
class HomeController {
  + HomeController() 
  + home() String
}
class Passenger {
  + Passenger(String, String, String, int, String, String, String, String, String) 
  + Passenger() 
  - String password
  - List~Reservation~ reservations
  - String username
  - String id
  - int age
  - String gender
  - String middlename
  - String email
  - String firstname
  - String mobile
  - String lastname
   List~Reservation~ reservations
   String password
   String middlename
   String gender
   int age
   String email
   String lastname
   String firstname
   String username
   String id
   String mobile
}
class PassengerController {
  + PassengerController() 
  + updatePassenger(String, String, String, String, Integer, String, String, String) ResponseEntity~?~
  + getPassenger(String) ResponseEntity~?~
  + createPassenger(String, String, String, Integer, String, String, String, String, String) ResponseEntity~?~
  + deletePassenger(String) ResponseEntity~?~
}
class PassengerRepository {
<<Interface>>
  + findById(String) Optional~Passenger~
  + findByMobileAndPassword(String, String) Passenger
  + findByUsernameAndPassword(String, String) Passenger
}
class PassengerService {
  + PassengerService() 
  + createPassenger(String, String, String, int, String, String, String, String, String) ResponseEntity~?~
  + deleteReservation(Reservation, Passenger) void
  - updateFlightSeats(Flight) void
  + getPassenger(String) ResponseEntity~?~
  + updatePassenger(String, String, String, String, int, String, String, String) ResponseEntity~?~
  + deletePassenger(String) ResponseEntity~?~
}
class Plane {
  + Plane() 
  + Plane(String, int, String, String) 
  - int id
  - String model
  - int capacity
  - String manufacturer
   String description
   String manufacturer
   int id
   String model
   int capacity
}
class PlaneRepository {
<<Interface>>
  + findById(String) Optional~Plane~
}
class Reservation {
  + Reservation() 
  + Reservation(String, String, double, Passenger, List~Flight~) 
  - String fromLocation
  - double price
  - Passenger passenger
  - String reservationNumber
  - String destination
  - List~Flight~ flights
   String reservationNumber
   Passenger passenger
   String fromLocation
   List~Flight~ flights
   double price
   String destination
}
class ReservationController {
  + ReservationController() 
  + cancelReservation(String) ResponseEntity~?~
  + createReservation(String, List~String~) ResponseEntity~?~
  + updateReservation(String, List~String~, List~String~) ResponseEntity~?~
  + getReservation(String) ResponseEntity~?~
}
class ReservationRepository {
<<Interface>>
  + findByPassenger(Passenger) List~Reservation~
  + findAllByFlightsIn(List~Flight~) List~Reservation~
  + findByReservationNumber(String) Reservation
}
class ReservationService {
  + ReservationService() 
  + getReservation(String) ResponseEntity~?~
  + cancelReservation(String) ResponseEntity~?~
  - increaseAvailableFlightSeats(List~Flight~) void
  - getFlightList(List~String~) List~Flight~
  - calculatePrice(List~Flight~) double
  - reduceAvailableFlightSeats(List~Flight~) void
  - isTimeOverlapForSamePerson(String, List~Flight~) boolean
  + createReservation(String, List~String~) ResponseEntity~?~
  + updateReservation(String, List~String~, List~String~) ResponseEntity~?~
  - isTimeOverlapWithinReservation(List~Flight~) boolean
  - isSeatsAvailable(List~Flight~) boolean
}
class Response {
  + Response() 
  + Response(int, String) 
  - int code
  - String message
  + toString() String
  + equals(Object) boolean
  + hashCode() int
  # canEqual(Object) boolean
   int code
   String message
}

