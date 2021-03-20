import Airline from "../airline/airline.model";
import Airport from "../airport/airport.model";

export default class Flight {
  constructor(
    public id: number,
    public flightId: string,
    public departureAirport: Airport,
    public arrivalAirport: Airport,
    public airline: Airline
  ) {}
}
