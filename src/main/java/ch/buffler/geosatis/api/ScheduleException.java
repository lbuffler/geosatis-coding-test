package ch.buffler.geosatis.api;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents an exception for a specific date on a schedule.
 * 
 * @author Laurent Buffler
 *
 */
public class ScheduleException {
	private DateTime date;
	private LocalTime startTime;
	private LocalTime endTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="HH:mm")
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	
	
}
