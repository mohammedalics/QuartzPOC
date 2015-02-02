package poc.quartz.sendemail;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.DateBuilder.todayAt;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;


import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailMain {
	/**
	 * This is to run the schedule  
	 * @throws Exception
	 */
	public void run() throws Exception {
		
		Logger log = LoggerFactory.getLogger(SendEmailMain.class);
		
		log.info("------- Initializing ----------------------");
		// First we must get a reference to a scheduler
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		log.info("------- Initialization Complete -----------");
		
		// start date, today 2:00
		Date runTime = todayAt(2, 0, 0);
		
		log.info("------- Scheduling Job -------------------");
		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(SendEmailJob.class).withIdentity("job1", "group1")
				.build();
		
		// Trigger the job to run on the next round minute
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1")
				.startAt(runTime).withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();
		
		// Tell quartz to schedule the job using our trigger
		sched.scheduleJob(job, trigger);
		log.info(job.getKey() + " will run at: " + runTime);
		
		// Start up the scheduler (nothing can actually run until the
		// scheduler has been started)
		sched.start();
		log.info("------- Started Scheduler -----------------");
		
		// wait long enough so that the scheduler as an opportunity to
		// run the job!
		log.info("------- Waiting 65 seconds... -------------");
		try {
			// wait 65 seconds to show job
			Thread.sleep(6500L * 1000L);
			// executing...
		} catch (Exception e) {
			//
		}
		
		// shut down the scheduler
		log.info("------- Shutting Down ---------------------");
		sched.shutdown(true);
		log.info("------- Shutdown Complete -----------------");
	}

	public static void main(String[] args) throws Exception {
		SendEmailMain example = new SendEmailMain();
		example.run();
	}
}
