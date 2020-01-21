package at.gwt.trainsim.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import at.gwt.trainsim.exception.AlreadyInUseException;
import lombok.Data;

/**
 * Represents a train that at least has a {@link Locomotive} and belongs to a
 * {@link RailroadCompany}.
 * 
 * Instances can be created by calling
 * {@link Train.Service#newTrain(RailroadCompany, Locomotive)}.
 * 
 * @author gotthardwitsch
 *
 */
@Data
public class Train {
	private static final Weight WEIGHT_PER_PASSENGER = new Weight(75);

	private final Set<Locomotive> locomotives;
	private final Set<Wagon> wagons;
	private RailroadCompany company;
	private Service service;

	static class Service {
		private static final Service instance = new Service();

		private Collection<Train> allTrains;

		private Service() {
			// singleton
			this.allTrains = new HashSet<>();
		}

		/**
		 * Creates a new train instance. Note that locomotives and wagons can only be
		 * part of one train, otherwise a {@link AlreadyInUseException} will be thrown.
		 * 
		 * @param company    the company managing the train
		 * @param locomotive the locomotive for the train
		 * @return a new train
		 */
		public static Train newTrain(RailroadCompany company, Locomotive locomotive) {
			instance.validate(locomotive);

			Train train = new Train(company, locomotive, instance);
			instance.allTrains.add(train);

			return train;
		}

		/**
		 * @param locomotive the locomotive to check
		 * @return true if the given locomotive is already used in a train
		 */
		private boolean isUsed(Locomotive locomotive) {
			return this.allTrains.stream().map(Train::getLocomotives).flatMap(Collection::stream)
					.anyMatch(locomotive::equals);
		}

		/**
		 * @param <T>   specific wagon
		 * @param wagon the wagon to check
		 * @return true if the given wagon is already used in a train
		 */
		private <T extends Wagon> boolean isUsed(T wagon) {
			return this.allTrains.stream().map(Train::getWagons).flatMap(Collection::stream).anyMatch(wagon::equals);
		}

		private void validate(Locomotive locomotive) {
			if (this.isUsed(locomotive)) {
				throw new AlreadyInUseException("Locomotive cannot be added to train. It is already in use.");
			}
		}

		private <T extends Wagon> void validate(T wagon) {
			if (this.isUsed(wagon)) {
				throw new AlreadyInUseException("Wagon cannot be added to train. It is already in use.");
			}
		}
	}

	private Train(RailroadCompany company, Locomotive locomotive, Service service) {
		this.company = Objects.requireNonNull(company, "Railroad company is required for a train");
		this.locomotives = new LinkedHashSet<>();
		this.wagons = new LinkedHashSet<>();
		this.service = service;

		this.addLocomotive(Objects.requireNonNull(locomotive, "Locomotive is required for a train"));
	}

	public boolean addLocomotive(Locomotive locomotive) {
		this.service.validate(locomotive);
		return this.locomotives.add(locomotive);
	}

	public boolean addWagon(Wagon wagon) {
		this.service.validate(wagon);
		return this.wagons.add(wagon);
	}

	public Collection<Locomotive> getLocomotives() {
		return Collections.unmodifiableSet(this.locomotives);
	}

	public Collection<Wagon> getWagons() {
		return Collections.unmodifiableSet(this.wagons);
	}

	public boolean removeLocomotive(Locomotive locomotive) {
		if (this.locomotives.size() == 1) {
			throw new IllegalStateException("Cannot remove last locomotive from train");
		}

		return this.locomotives.remove(locomotive);
	}

	public boolean removeWagon(Wagon wagon) {
		return this.wagons.remove(wagon);
	}

	/**
	 * @return empty weight of this train
	 */
	public Weight getEmpyWeight() {
		return new Weight(this.sumEmptyWeight(this.locomotives) + this.sumEmptyWeight(this.wagons));
	}

	/**
	 * @return the maximum number of passengers that can be transported
	 */
	public long getMaxPassengers() {
		return this.sumMaxPassengers(this.locomotives) + this.sumMaxPassengers(this.wagons);
	}

	/**
	 * @return the maximum additional freight that can be transported
	 */
	public Weight getMaxAdditionalFreight() {
		return new Weight(this.wagons.stream().map(RailVehicle::getMaxAdditionalLoad).map(Weight::getValue)
				.mapToDouble(Double::valueOf).sum());
	}

	/**
	 * @return the maximum additional load for passengers and freight that can be
	 *         transported
	 */
	public Weight getMaxAdditionalLoad() {
		return new Weight(
				this.getMaxPassengers() * WEIGHT_PER_PASSENGER.getValue() + this.getMaxAdditionalFreight().getValue());
	}

	/**
	 * @return the total weight of this train
	 */
	public Weight getWeight() {
		return new Weight(this.getEmpyWeight().getValue() + this.getMaxAdditionalLoad().getValue());
	}

	/**
	 * @return the total length of this train
	 */
	public Length getLength() {
		return new Length(this.sumLength(this.locomotives) + this.sumLength(this.wagons));
	}

	/**
	 * @return the total tractive force for this train
	 */
	public Weight getTractiveForce() {
		return new Weight(this.locomotives.stream().map(Locomotive::getTractiveForce).map(Weight::getValue)
				.mapToDouble(Double::valueOf).sum());
	}

	/**
	 * @return true if the locomotives are able to pull the maximum additional load
	 */
	public boolean isDrivable() {
		return this.getTractiveForce().getValue() > this.getMaxAdditionalLoad().getValue();
	}

	/**
	 * @return true if there are more than 0 passengers allowed
	 */
	public boolean requiresGuard() {
		return this.getMaxPassengers() > 0;
	}

	/**
	 * @return the number of required guards. 1 guard is required per 50 passengers.
	 */
	public int getRequiredGuards() {
		return Double.valueOf(Math.ceil(this.getMaxPassengers() / 50d)).intValue();
	}

	private double sumEmptyWeight(Collection<? extends RailVehicle> vehicles) {
		return vehicles.stream().map(RailVehicle::getEmptyWeight).map(Weight::getValue).mapToDouble(Double::valueOf)
				.sum();
	}

	private long sumMaxPassengers(Collection<? extends RailVehicle> vehicles) {
		return vehicles.stream().map(RailVehicle::getMaxPassengers).mapToLong(Long::valueOf).sum();
	}

	private long sumLength(Collection<? extends RailVehicle> vehicles) {
		return vehicles.stream().map(RailVehicle::getLength).map(Length::getValue).mapToLong(Long::valueOf).sum();
	}
}
