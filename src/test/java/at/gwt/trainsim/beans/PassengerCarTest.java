package at.gwt.trainsim.beans;

import org.junit.Assert;
import org.junit.Test;

public class PassengerCarTest {

	@Test(expected = IllegalArgumentException.class)
	public void testPassengerCar() {
		PassengerCar passengerCar = new PassengerCar(new Weight(1000), new Length(60), -10, new Weight(2000),
				new Classification("PassengerCar"), new Manufacturer("Siemens"), new YearOfManufacturing(1990),
				new SerialNumber("ASDF123"));

		Assert.assertNotNull(passengerCar);
	}
}
