package ch.buffler.geosatis.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a schedule associated with a zone.
 * @author Laurent Buffler
 *
 */
public class Schedule {
	private long id;
	private String name;
	private String zoneId;
	private final List<TimeRange> ranges = new ArrayList<>();
	private final List<ScheduleException> exceptions = new ArrayList<>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public List<TimeRange> getRanges() {
		return ranges;
	}
	public List<ScheduleException> getExceptions() {
		return exceptions;
	}
	
	
}
