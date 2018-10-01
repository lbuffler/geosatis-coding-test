package ch.buffler.geosatis.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;

import ch.buffler.geosatis.api.Schedule;
import ch.buffler.geosatis.api.ScheduleException;
import ch.buffler.geosatis.api.ScheduleManager;
import ch.buffler.geosatis.api.TimeRange;
import ch.buffler.geosatis.api.ZoneStatus;

/**
 * Implementations of a ScheduleManager that keeps schedules in memory.
 * 
 * @author Laurent Buffler
 *
 */
public class InMemoryScheduleManager implements ScheduleManager {
	/**
	 * Counter used to generated ids as new schedules are inserted
	 */
	private static final AtomicLong idCounter = new AtomicLong(1);
	/**
	 * Storage for schedules
	 */
	private static final ConcurrentHashMap<Long, Schedule> schedules = new ConcurrentHashMap<>();
	
	/**
	 * Get next id and increment the counter.
	 * This is guaranteed to return a different id each time it is
	 * called.
	 * @return next id
	 */
	private long getNextScheduleId() {
		return idCounter.getAndIncrement();
	}

	public Schedule find(String zoneId, long id) {
		Schedule schedule = schedules.get(id);
		if (schedule == null || !schedule.getZoneId().equals(zoneId)) {
			return null;
		}
		return schedule;
	}


	public long create(String zoneId, Schedule schedule) {
		long id = getNextScheduleId();
		schedule.setId(id);
		schedule.setZoneId(zoneId);
		schedules.put(id, schedule);
		return id;
	}

	public void update(String zoneId, long id, Schedule schedule) {
		Schedule existing = find(zoneId, id);
		if (existing == null) {
			throw new IllegalArgumentException("Cannot find schedule");
		}
		// TODO check if it is in past, rejects the update if it's the case
		schedules.put(id, schedule);
	}

	public void delete(String zoneId, long id) {
		// TODO Not implemented yet

	}

	public void addException(String zoneId, long id, ScheduleException override) {
		Schedule schedule = find(zoneId, id);
		if (schedule == null) throw new IllegalArgumentException("Schedule doesn't exist");
		schedule.getExceptions().add(override);
	}

	public ZoneStatus getZoneStatusForDateTime(String zoneId, DateTime dateTime) {
		var result = getAllSchedulesForZoneId(zoneId).anyMatch(schedule -> isDateTimeInSchedule(dateTime, schedule));
		return new ZoneStatus(result);
	}
	
	/**
	 * Gets all schedules for a given zone.
	 * @param zoneId
	 * @return a stream of the schedules of the zone
	 */
	private Stream<Schedule> getAllSchedulesForZoneId(String zoneId) {
		return schedules.values().stream().filter(schedule -> schedule.getZoneId().equals(zoneId));
	}
	
	/**
	 * Checks if the given date/time is included in a schedule. 
	 * To be considered part of a schedule, a date/time must either:
	 * - match the date of an exception, and be inside its time range. If the exception
	 *   doesn't define one, then the given date/time is not part of this schedule
	 * - be part of a range
	 * 
	 *  If an exception exists for the given date, result depends only on it, ranges
	 *  won't be checked at all.
	 *  
	 * @param dateTime date/time to check
	 * @param schedule
	 * @return true if schedule includes date/time
	 */
	private boolean isDateTimeInSchedule(DateTime dateTime, Schedule schedule) {

		for(ScheduleException override : schedule.getExceptions()) {
			DateTime overrideStartTime = override.getDate();
			long difference = dateTime.getMillis() - overrideStartTime.getMillis();
			if (difference > 0 && difference < DateTimeConstants.MILLIS_PER_DAY) {
				if (override.getStartTime() == null) return false;
				DateTime absoluteStartTime = override.getStartTime().toDateTime(overrideStartTime);
				DateTime absoluteEndTime = override.getEndTime().toDateTime(overrideStartTime);
				return dateTime.isAfter(absoluteStartTime) && dateTime.isBefore(absoluteEndTime);
			}
		}
		
		for(TimeRange range : schedule.getRanges()) {
			DateTime rangeStartTime = range.getStartTime();
			DateTime rangeEndTime = range.getEndTime();
			if (range.getRepeatInterval() > 0) {
				int repeatCount = Days.daysBetween(rangeStartTime, dateTime).getDays() /range.getRepeatInterval();
				if (repeatCount > 0) {
					rangeStartTime = rangeStartTime.plusDays(repeatCount * range.getRepeatInterval());
					rangeEndTime = rangeEndTime.plusDays(repeatCount * range.getRepeatInterval());
				}
			}
			if (dateTime.isAfter(rangeStartTime) && dateTime.isBefore(rangeEndTime)) return true;
		}
		
		return false;
	}
}
