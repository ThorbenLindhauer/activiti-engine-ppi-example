package de.thorben.ppi;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class SendConfirmationDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Order order = (Order) execution.getVariable("Order");
		System.out.println("Order confirmed. Order value: " + order.getValue());
	}
}
