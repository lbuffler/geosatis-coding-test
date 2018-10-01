package ch.buffler.geosatis.api;

/**
 * Represents the status of a zone at a specific time. It is used
 * to know if the offender is supposed to be inside the zone at
 * this time. 
 * @author Laurent Buffler
 *
 */
public class ZoneStatus {
	private final boolean mustBeInZone;

	public ZoneStatus(boolean mustBeInZone) {
		this.mustBeInZone = mustBeInZone;
	}

	public boolean isMustBeInZone() {
		return mustBeInZone;
	}
	
}
