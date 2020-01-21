package at.gwt.trainsim.beans;

import lombok.Data;

/**
 * Represents a weight in kilogram.
 * 
 * @author gotthardwitsch
 *
 */
@Data
public class Weight {
	private final double value;

	public Weight(double value) {
		if (value < 0) {
			throw new IllegalArgumentException("Weight cannot be less than 0");
		}

		this.value = value;
	}
}
