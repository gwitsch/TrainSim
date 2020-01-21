package at.gwt.trainsim.beans;

import lombok.Data;

/**
 * Represents vehicle that requires rails to be moved. Each vehicle has an empty
 * weight in kilogram.
 * 
 * @author gotthardwitsch
 *
 */
@Data
public abstract class RailVehicle {

	private final Weight emptyWeight;
	private final Length length;
	private final long maxPassengers;
	private final Weight maxAdditionalLoad;
	private final Classification classification;
	private final Manufacturer manufacturer;
	private final YearOfManufacturing yearOfManufacturing;
	private final SerialNumber serialNumber;

	public RailVehicle(Weight emptyWeight, Length length, long maxPassengers, Weight maxAdditionalLoad, Classification classification,
			Manufacturer manufacturer, YearOfManufacturing yearOfManufacturing, SerialNumber serialNumber) {
		super();

		if (maxPassengers < 0) {
			throw new IllegalArgumentException("MaxPassengers cannot be less than 0");
		}

		this.emptyWeight = emptyWeight;
		this.length = length;
		this.maxPassengers = maxPassengers;
		this.maxAdditionalLoad = maxAdditionalLoad;
		this.classification = classification;
		this.manufacturer = manufacturer;
		this.yearOfManufacturing = yearOfManufacturing;
		this.serialNumber = serialNumber;
	}

}
