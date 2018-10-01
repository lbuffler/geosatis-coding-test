package ch.buffler.geosatis.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchedulePatchRequest {
	@JsonProperty(required=true)
	public String action;
	public ScheduleException exception;
	public final static String ADD_EXCEPTION = "addException";
}
