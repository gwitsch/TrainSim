package at.gwt.trainsim.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a length in meters.
 * 
 * @author gotthardwitsch
 *
 */
@Data
@AllArgsConstructor
public class Length {
	private final long value;
}
