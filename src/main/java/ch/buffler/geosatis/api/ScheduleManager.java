package ch.buffler.geosatis.api;

import org.joda.time.DateTime;

/**
 * Component used to work on schedule.
 * @author Laurent Buffler
 *
 */
public interface ScheduleManager {
	/**
	 * Finds the schedule based on its zone and id.
	 * @param zoneId zone id 
	 * @param id schedule id
	 * @return a schedule, or null if none is found
	 */
	Schedule find(String zoneId, long id);
	/**
	 * Saves a new schedule in a zone. A unique id will be generated for it.
	 * @param zoneId zone id
	 * @param schedule
	 * @return the id of the saved schedule
	 */
	long create(String zoneId, Schedule schedule);
	/**
	 * Replaces an existing schedule.
	 * @param zoneId
	 * @param id
	 * @param schedule
	 * @throws IllegalArgumentException if no schedule exists with the given id inside the zone,
	 * or if it is in the past.
	 */
	void update(String zoneId, long id, Schedule schedule);
	void delete(String zoneId, long id);
	/**
	 * Adds a new exception on an existing schedule. 
	 * @param zoneId
	 * @param id
	 * @param exception
	 * @throws IllegalArgumentException if no schedule exists with the given id inside the zone
	 */
	void addException(String zoneId, long id, ScheduleException exception);
	/**
	 * Computes the status of the zone at the given time. The returned
	 * status indicates if the offender is supposed to be inside the zone at this time.
	 * @param zoneId zone id
	 * @param dateTime date/time for which to get the status
	 * @return status of zone at the given time
	 */
	ZoneStatus getZoneStatusForDateTime(String zoneId, DateTime dateTime);
}
