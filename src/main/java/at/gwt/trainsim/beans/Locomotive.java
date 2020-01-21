package at.gwt.trainsim.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a locomotive. Each locomotive has got an empty weight and tractive
 * force describing how much weight in kilogram can be pulled additionally.
 * 
 * @author gotthardwitsch
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Locomotive extends RailVehicle {

	private final Weight tractiveForce;

	private final Drive typeOfDrive;

	/**
	 * @param emptyWeight         the locomotive's own weight
	 * @param tractiveForce       the weight that this locomotive can pull
	 * @param length              the length of this locomotive
	 * @param maxPassengers       the number of passengers that can be transported
	 * @param maxAdditionalLoad   the additional weight that can be transported
	 * @param classification      the classification for this locomotive
	 * @param manufacturer        this company which build this locomotive
	 * @param yearOfManufacturing the year this locomotive has been built in
	 * @param serialNumber        the locomotive's serial number
	 * @param typeOfDrive         the locomotive's type of drive
	 */
	public Locomotive(Weight emptyWeight, Weight tractiveForce, Length length, long maxPassengers,
			Weight maxAdditionalLoad, Classification classification, Manufacturer manufacturer,
			YearOfManufacturing yearOfManufacturing, SerialNumber serialNumber, Drive typeOfDrive) {
		super(emptyWeight, length, maxPassengers, maxAdditionalLoad, classification, manufacturer, yearOfManufacturing,
				serialNumber);

		this.tractiveForce = tractiveForce;
		this.typeOfDrive = typeOfDrive;
	}

	/**
	 * Creates a locomotive that <b>cannot</b> transport any passengers or any
	 * additional load.
	 * 
	 * @param emptyWeight         the locomotive's own weight
	 * @param tractiveForce       the weight that this locomotive can pull
	 * @param length              the length of this locomotive
	 * @param classification      the classification for this locomotive
	 * @param manufacturer        this company which build this locomotive
	 * @param yearOfManufacturing the year this locomotive has been built in
	 * @param serialNumber        the locomotive's serial number
	 * @param typeOfDrive         the locomotive's type of drive
	 */
	public Locomotive(Weight emptyWeight, Weight tractiveForce, Length length, Classification classification,
			Manufacturer manufacturer, YearOfManufacturing yearOfManufacturing, SerialNumber serialNumber,
			Drive typeOfDrive) {
		this(emptyWeight, tractiveForce, length, 0L, new Weight(0), classification, manufacturer, yearOfManufacturing,
				serialNumber, typeOfDrive);
	}

}
