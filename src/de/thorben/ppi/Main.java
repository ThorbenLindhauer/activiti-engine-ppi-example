package de.thorben.ppi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import de.unipotsdam.hpi.thorben.ppi.engine.PPIProcessEngine;
import de.unipotsdam.hpi.thorben.ppi.service.PPIService;

public class Main {

	private static final String PPI_ENGINE_CONFIG = "activiti.h2.cfg.xml";
	private static int INSTANCES = 20;
	private static int AVERAGE_ORDER_VALUE = 6000;
	private static int ORDER_VALUE_VARIATION = 5500;
	private static int SIMULATED_TASK_EXECUTION_TIME = 10; 		// in seconds
	
	private static PPIProcessEngine engine;

	public static void main(String[] args) throws InterruptedException {
		try {
			parseArguments(args);
		} catch (PPIExampleException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		engine = (PPIProcessEngine) ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource(PPI_ENGINE_CONFIG)
				.buildProcessEngine();
		
		createUsers();
		deployDefinition();
		
		startProcessInstances(); 	// runs until the first human task is created, if any
		
		completeTasksForUser("clerk");
		completeTasksForUser("customer");
		
		evaluatePPIs();

	}
	
	private static void parseArguments(String[] args) throws PPIExampleException {
		if (args.length != 0 && args.length != 4) {
			throw new PPIExampleException("Requires 0 arguments or 4 arguments(numbers) in format: instances task_execution_time average_order_value order_value_variation");
		}
		if (args.length == 4) {
			INSTANCES = Integer.parseInt(args[0]);
			SIMULATED_TASK_EXECUTION_TIME = Integer.parseInt(args[1]);
			AVERAGE_ORDER_VALUE = Integer.parseInt(args[2]);
			ORDER_VALUE_VARIATION = Integer.parseInt(args[3]);
		} 
	}

	private static void evaluatePPIs() {
		PPIService ppiService = engine.getPPIService();
		// PPI 1
		Number result = ppiService.calculatePPI("ppi1", "scenario");
		System.out.println("Average execution time is " + result + " milliseconds.");
		boolean fulfilled = ppiService.PPIfulfilled("ppi1", "scenario");
		System.out.println("PPI 1 was fulfilled: " + fulfilled + ".");
		
		// PPI 2
		result = ppiService.calculatePPI("ppi2", "scenario");
		System.out.println("Ratio of assisted/processed instances is " + result + ".");
		fulfilled = ppiService.PPIfulfilled("ppi2", "scenario");
		System.out.println("PPI 2 was fulfilled: " + fulfilled + ".");
		
		// PPi 3
		result = ppiService.calculatePPI("ppi3", "scenario");
		System.out.println("Sum of all order values is " + result + ".");
		fulfilled = ppiService.PPIfulfilled("ppi3", "scenario");
		System.out.println("PPI 3 was fulfilled: " + fulfilled + ".");
		
	}

	private static void completeTasksForUser(String userId) throws InterruptedException {
		TaskService taskService = engine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).list();
		List<Thread> taskCompletionThreads = new ArrayList<Thread>();
		
		for (Task task : tasks) {
			Thread t = createTaskCompletionThread(taskService, task.getId());
			t.start();
			taskCompletionThreads.add(t);
		}
		
		for (Thread t : taskCompletionThreads) {
			t.join();
		}
		
	}

	private static Order createOrder(int orderValue) {
		Order order = new Order();
		order.setValue(orderValue);
		return order;
	}

	private static void startProcessInstances() throws InterruptedException {
		RuntimeService runtime = engine.getRuntimeService();
		
		
		List<Thread> instantiationThreads = new ArrayList<Thread>();
		for (int i = 0; i < INSTANCES; i++) {
			int variableOrderValue = (int) (Math.random() * ORDER_VALUE_VARIATION * 2);
			int orderValue = AVERAGE_ORDER_VALUE - ORDER_VALUE_VARIATION + variableOrderValue;
			Order order = createOrder(orderValue);
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("Order", order);
			
			
			Thread t = createInstantiationThread(runtime, "scenario", variables);
			t.start();
			instantiationThreads.add(t);
		}
		
		for (Thread t : instantiationThreads) {
			t.join();
		}
	}

	private static void createUsers() {
		IdentityService identityService = engine.getIdentityService();
		identityService.newUser("customer");
		identityService.newUser("clerk");
	}

	private static void deployDefinition() {
		RepositoryService repoService = engine.getRepositoryService();
		repoService.createDeployment()
				.addClasspathResource("scenario.bpmn20.xml").deploy();
	}
	
	private static Thread createInstantiationThread(final RuntimeService runtime, final String processDefinitionKey, final Map<String, Object> variables) {

		return new Thread(new Runnable() {
			public void run() {
				runtime.startProcessInstanceByKey(processDefinitionKey, variables);
			}
		});
	}
	
	private static Thread createTaskCompletionThread(final TaskService taskService, final String taskId) {

		return new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(SIMULATED_TASK_EXECUTION_TIME * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 			
				taskService.complete(taskId);
			}
		});
	}

}
