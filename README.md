[activitiPPIRepo]: https://github.com/ThorbenLindhauer/activiti-engine-ppi
[scenarioImage]: https://raw.github.com/ThorbenLindhauer/activiti-engine-ppi-example/master/scenario.png "The scenario process"
[scenarioXml]: https://github.com/ThorbenLindhauer/activiti-engine-ppi-example/blob/master/src/scenario.bpmn20.xml
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

The serialized process can be found [here][scenarioXml]. 
The PPI-related extension elements are explained on the readme of the [engine][activitiPPIRepo].

Demo application
================

To run the demo application, you can either checkout the sources or use a pre-built .jar (to be uploaded).
If you choose to build and run it from the sources, you also have to build the [engine][activitiPPIRepo] yourself.
