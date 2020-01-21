package at.gwt.trainsim.beans;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.gwt.trainsim.exception.AlreadyInUseException;

public class TrainTest {
	private static final RailroadCompany COMPANY = new RailroadCompany("OEBB");
	private static final Manufacturer MANUFACTURER = new Manufacturer("Siemens");

	private Locomotive loc;

	@Before
	public void setup() {
		Weight emptyWeight = new Weight(30_000);
		Weight tractiveForce = new Weight(150_000);
		Length length = new Length(30);
		Classification classification = new Classification("Taurus");
		YearOfManufacturing yearOfManufacturing = new YearOfManufacturing(2005);
		SerialNumber serialNumber = new SerialNumber("Serial-" + System.nanoTime());

		this.loc = new Locomotive(emptyWeight, tractiveForce, length, 4, new Weight(0), classification, MANUFACTURER,
				yearOfManufacturing, serialNumber, Drive.ELECTRICITY);
	}

	@Test
	public void testTrain() {
		Train train = Train.Service.newTrain(COMPANY, this.loc);

		Assert.assertEquals(new Weight(300), train.getMaxAdditionalLoad());
		Assert.assertEquals(0, train.getMaxAdditionalFreight().getValue(), 0);

		PassengerCar passengerCar1 = new PassengerCar(new Weight(20_000), new Length(50), 120, new Weight(12_000), null,
				MANUFACTURER, new YearOfManufacturing(2000), new SerialNumber("1"));
		PassengerCar passengerCar2 = new PassengerCar(new Weight(20_000), new Length(50), 120, new Weight(12_000), null,
				MANUFACTURER, new YearOfManufacturing(2000), new SerialNumber("2"));

		train.addWagon(passengerCar1);
		train.addWagon(passengerCar2);

		Assert.assertEquals(2, train.getWagons().size());
		Assert.assertTrue(train.isDrivable());
		Assert.assertTrue(train.requiresGuard());
		Assert.assertEquals(5, train.getRequiredGuards());
		Assert.assertEquals(244, train.getMaxPassengers());
		Assert.assertEquals(new Weight(42_300), train.getMaxAdditionalLoad());
		Assert.assertEquals(new Weight(70_000), train.getEmpyWeight());
		Assert.assertEquals(new Length(130), train.getLength());
		Assert.assertEquals(new Weight(112_300), train.getWeight());

		train.removeWagon(passengerCar2);
		Assert.assertEquals(1, train.getWagons().size());
		Assert.assertTrue(train.getWagons().contains(passengerCar1));

		train.addWagon(new SleepingCar(new Weight(30_000), new Length(70), 60, new Weight(20_000),
				new Classification("Comfortable Beds"), new Manufacturer("Heavens Bedrooms"),
				new YearOfManufacturing(2005), new SerialNumber("Beds #1")));

		train.addWagon(new DiningCar(new Weight(50_000), new Length(70), 80, new Weight(5_000),
				new Classification("Food & Beverage"), new Manufacturer("Siemens"), new YearOfManufacturing(2009),
				new SerialNumber("FoodWagon123")));

		Assert.assertEquals(3, train.getWagons().size());
	}

	@Test(expected = AlreadyInUseException.class)
	public void testAddLocomotiveTwice() {
		Train train = Train.Service.newTrain(COMPANY, this.loc);
		train.addLocomotive(this.loc);
	}

	@Test(expected = AlreadyInUseException.class)
	public void testAddLocomotiveToAnotherTrain() {
		Train.Service.newTrain(COMPANY, this.loc);
		Train.Service.newTrain(COMPANY, this.loc);
	}

	@Test(expected = AlreadyInUseException.class)
	public void testAddWagonTwice() {
		Train train = Train.Service.newTrain(COMPANY, this.loc);

		PassengerCar passengerCar = new PassengerCar(new Weight(20_000), new Length(50), 120, new Weight(12_000), null,
				MANUFACTURER, new YearOfManufacturing(2000), new SerialNumber("3"));

		train.addWagon(passengerCar);
		train.addWagon(passengerCar);
	}

	@Test(expected = AlreadyInUseException.class)
	public void testAddWagonToAnpotherTrain() {
		Train train = Train.Service.newTrain(COMPANY, this.loc);

		PassengerCar passengerCar = new PassengerCar(new Weight(20_000), new Length(50), 120, new Weight(12_000), null,
				MANUFACTURER, new YearOfManufacturing(2000), new SerialNumber("4"));
		train.addWagon(passengerCar);

		Train oldTrain = Train.Service.newTrain(COMPANY,
				new Locomotive(new Weight(20_000), new Weight(80_000), new Length(40), 2, new Weight(0),
						new Classification("Old-Timer"), MANUFACTURER, new YearOfManufacturing(1830),
						new SerialNumber("OldTrain"), Drive.DIESEL));

		oldTrain.addWagon(passengerCar);
	}

	@Test
	public void testNotDrivableTrain() {
		Train oldTrain = Train.Service.newTrain(COMPANY,
				new Locomotive(new Weight(20_000), new Weight(8_000), new Length(20), 2, new Weight(0),
						new Classification("Old-Timer"), MANUFACTURER, new YearOfManufacturing(1720),
						new SerialNumber("VeryOldTrain"), Drive.STEAM));

		PassengerCar passengerCar = new PassengerCar(new Weight(20_000), new Length(50), 120, new Weight(12_000), null,
				MANUFACTURER, new YearOfManufacturing(2000), new SerialNumber("5"));
		oldTrain.addWagon(passengerCar);

		Assert.assertFalse(oldTrain.isDrivable());
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveLocomotive() {
		Train train = Train.Service.newTrain(COMPANY, this.loc);
		train.removeLocomotive(this.loc);
	}

	@Test
	public void testNoGuardRequired() {
		Locomotive loc = new Locomotive(new Weight(40_000), new Weight(300_000), new Length(60), 0, new Weight(0),
				new Classification(""), MANUFACTURER, new YearOfManufacturing(2020),
				new SerialNumber("Brand New Train #1"), Drive.ELECTRICITY);

		Train train = Train.Service.newTrain(COMPANY, loc);

		train.addWagon(new FreightWagon(new Weight(15_000), new Length(120), 0, new Weight(80_000),
				new Classification("Loads wood"), new Manufacturer("SteelWagon"), new YearOfManufacturing(1990),
				new SerialNumber("Wood1")));

		Assert.assertFalse(train.requiresGuard());

		Locomotive loc2 = new Locomotive(new Weight(40_000), new Weight(300_000), new Length(60),
				new Classification(""), MANUFACTURER, new YearOfManufacturing(2020),
				new SerialNumber("Brand New Train #2"), Drive.ELECTRICITY);

		train.addLocomotive(loc2);
		Assert.assertEquals(2, train.getLocomotives().size());

		train.removeLocomotive(loc);
		Assert.assertEquals(1, train.getLocomotives().size());
		Assert.assertTrue(train.getLocomotives().contains(loc2));

	}

}
