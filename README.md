# University Timetable Planner

This is going to be an application for managing timetable of a university. The app is being developed in a step by step fashion.
1. First step was to create a UML class diagram of the project. In that diagram you can see the general idea of what main entities in the project are and
how they interact with each other. You can find the initial UML diagram [here](https://github.com/kishington/uml-decomposition/blob/master/university-decomposition.png).
Of course, as the project is being developed there could be some changes to the original concept of the application and new features could be added in the future.
2. Second step was to implement in Java code all the classes described in the UML class diagram.
3. Third step was adding DAO layer based on Spring JDBC.
4. Fourth step was to add service layer and implementation of business logic using Spring IoC. Some functionality has been added already but as the project develops, there is an
increasing need to add new functionality. That means that in the future we might go back to this step again and again.
5. Fifth step is to add custom logging and Exception handling. I haven't finished with this step yet. There is some minor polishing to be done.
6. Sixth step is to add User Interface based on Spring MVC, Thymeleaf and Bootstrap. I have just stated working on it while polishing some bits of the logging.
7. There is lots of work to be done in the future. There are so many technologies and frameworkds to be added. Specifically, such technologies as Hibernate, Spring Boot,
Spring Data JPA, REST, Swagger, Spring Boot Tests and others.
