[activitiPPIRepo]: https://github.com/ThorbenLindhauer/activiti-engine-ppi
[scenarioImage]: https://raw.github.com/ThorbenLindhauer/activiti-engine-ppi-example/master/scenario.png "The scenario process"
[scenarioXml]: https://github.com/ThorbenLindhauer/activiti-engine-ppi-example/blob/master/src/scenario.bpmn20.xml
[exampleApp]: https://github.com/downloads/ThorbenLindhauer/activiti-engine-ppi-example/exampleApp.jar
This project illustrates how to measure performance indicator on BPMN processes using the [PPI-enhanced Activiti process engine][activitiPPIRepo].

Scenario
========

The scenario concerns an online hardware store that assists customers with buying the right products.

![alt text][scenarioImage]

The process begins with the customer delivering an order. 
If the order value is greater than 2000 € or the customer requests it, a hardware store cleark looks at the order and
provides the customer with advice, for example if he ordered the correct screws for his new cupboard.
The customer may then review his order and update it and finally receives an order confirmation.
Payment and delivery are not part of this process.

The defined PPIs:

1. PPI 1 measures the overall execution time and aggregates the average overall process instances. It sets less than 60 seconds as a target value.
2. PPI 2 involves two Count Measures: one that counts all process instances and the other that counts the instances that involve human assistance. It then applies a derivation function and asserts that the fraction of assisted cases is less than 60 %.
3. PPI 3 refers to a Data Measure and measures the value of the incoming orders. It is specified to measure and sum the "value" field of the "order" data object over all process instances. As Activiti does not treat modelled data objects, this relies to java objects that are identified by the data object name (here "Order") in the execution context.

The serialized process can be found [here][scenarioXml]. 
The PPI-related extension elements are explained on the readme of the [engine][activitiPPIRepo].

Demo application
================

To run the demo application, you can either checkout the sources or use a pre-built [jar][exampleApp].
If you choose to build and run it from the sources, you also have to build the [engine][activitiPPIRepo] yourself.

The application takes 0 or 4 arguments. If 0, the arguments are set to default values. These are the arguments:

1. The number of process instances that are instantiated (20 by default)
2. The execution of the human tasks is simulated and this parameter specifies the time in seconds the execution of one task should take in seconds (10 by default).
3. The average order value (6000 by default)
4. The maximum variation of order values (5500 by default, so order values may vary from 500 to 11500 by default)

The last activity of the process is actually a service task that prints the order value to the console.

After all instances have finished, all PPIs are checked whether they are fulfilled and their values are written to the console.
You can use the parameters of the application to influence the PPIs and change the outcome.