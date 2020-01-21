package at.gwt.trainsim.beans;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * Represents a wagon with a restaurant for having dinner.
 * 
 * @author gotthardwitsch
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DiningCar extends Wagon {

	/**
	 * @param emptyWeight         the wagon's own weight
	 * @param length              the length of this wagon
	 * @param maxPassengers       the number of passengers that can be transported
	 * @param maxAdditionalLoad   the additional weight that can be transported
	 * @param classification      the classification for this locomotive
	 * @param manufacturer        this company which build this locomotive
	 * @param yearOfManufacturing the year this locomotive has been built in
	 * @param serialNumber        the locomotive's serial number
	 */
	public DiningCar(Weight emptyWeight, Length length, long maxPassengers, Weight maxAdditionalLoad, Classification classification,
			Manufacturer manufacturer, YearOfManufacturing yearOfManufacturing, SerialNumber serialNumber) {
		super(emptyWeight, length, maxPassengers, maxAdditionalLoad, classification, manufacturer, yearOfManufacturing,
				serialNumber);
	}

}
