package ch.buffler.geosatis;


import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ch.buffler.geosatis.api.ScheduleManager;
import ch.buffler.geosatis.impl.InMemoryScheduleManager;
import ch.buffler.geosatis.resources.ScheduleResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CodingTestApplication extends Application<CodingTestConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CodingTestApplication().run(args);
    }

    @Override
    public String getName() {
        return "CodingTest";
    }

    @Override
    public void initialize(final Bootstrap<CodingTestConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final CodingTestConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(ScheduleResource.class);
        environment.jersey().register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(InMemoryScheduleManager.class).to(ScheduleManager.class);
			}
		});
    }

}
