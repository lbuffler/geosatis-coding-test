package ch.buffler.geosatis.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import ch.buffler.geosatis.api.Schedule;
import ch.buffler.geosatis.api.ScheduleManager;
import ch.buffler.geosatis.api.SchedulePatchRequest;
import ch.buffler.geosatis.api.ZoneStatus;
import io.dropwizard.jersey.PATCH;

/**
 * Jersey resource for handling schedules.
 * @author Laurent Buffler
 *
 */
@Path("/v1.0/zones/{zoneId}")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduleResource {
	
	private final ScheduleManager manager;
	
	@Inject
	public ScheduleResource(ScheduleManager manager) {
		this.manager = manager;
	}
	
	@GET
	@Path("schedules/{id}")
	public Schedule get(@PathParam("zoneId") String zoneId, @PathParam("id") long id) {
		Schedule schedule = manager.find(zoneId, id);
		if (schedule == null) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		return schedule;
	}
	
	@POST
	@Path("schedules")
	public Response create(@PathParam("zoneId") String zoneId, Schedule schedule, @Context UriInfo uriInfo) {
		long id = manager.create(zoneId, schedule);
		return Response.created(uriInfo.getAbsolutePathBuilder().path("" + id).build()).build();
	}
	
	@PUT
	@Path("schedules/{id}")
	public Response update(@PathParam("zoneId") String zoneId, @PathParam("id") long id, Schedule schedule) {
		manager.update(zoneId, id, schedule);
		return Response.ok().build();
	}
	
	@PATCH
	@Path("schedules/{id}")
	public Response patch(@PathParam("zoneId") String zoneId, @PathParam("id") long id, SchedulePatchRequest request) {
		Schedule schedule = manager.find(zoneId, id);
		if (schedule == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		
		switch(request.action) {
		case "addException":
			if (request.exception == null) {
				return Response.status(Status.BAD_REQUEST).build();
			}
			manager.addException(zoneId, id, request.exception);
			return Response.ok().build();
			default:
				throw new WebApplicationException("Invalid patch action", Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("status")
	public ZoneStatus check(@PathParam("zoneId") String zoneId, @QueryParam("dateTime") String dateTimeString) {
		var parser = ISODateTimeFormat.dateTimeParser();
		final DateTime dateTime;
		try {
			dateTime = parser.parseDateTime(dateTimeString);
		} catch (IllegalArgumentException e) {
			throw new WebApplicationException(e);
		}
		ZoneStatus status = manager.getZoneStatusForDateTime(zoneId, dateTime);
		return status;
	}
	
}
