package at.gwt.trainsim.beans;

import org.junit.Test;

public class WeightTest {

	@Test(expected = IllegalArgumentException.class)
	public void testWeight() {
		new Weight(-1000);
	}

}
