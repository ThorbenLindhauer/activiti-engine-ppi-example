package de.thorben.ppi;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AnalyzeOrderDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Order order = (Order) execution.getVariable("Order");
		if (order.getValue() > 2000) {
			execution.setVariable("assistanceGranted", true);
		} else {
			execution.setVariable("assistanceGranted", false);
		}
	}

}
