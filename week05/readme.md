# 第一道必做题

Spring framework版本为5.1.13

#### 1.在xml中定义bean以及注入bean

-在xml中定义bean

#### 2.在xml中定义bean和开启注解配置。

##### 使用注解@Autowire来注入

@Autowire作用在setter方法。

@Autowire作用在成员变量。

@Autowire作用在构造方法中的参数，构造方法中匹配类型的参数都会被注入。

@Autowire作用在成员方法中，该成员方法会被自动调用。

@Autowire无法通过name或者id来指定注入一个bean，需要配合@Qualifier来指定一个bean。

使用@Resource（JSR中的注解）可以实现“自动注入某个指定bean”的效果。

本阶段总结：使用“xml + 注解”的方式来配置bean，相当于xml中专门负责定义BeanDefinition，而“注解”则负责依赖注入。

#### 3.JavaConfig

@Bean 方法级别的注释，和XML中的<bean/>标签一致。相当于到用一个类方法来实现<bean/>标签的功能。注解Bean创建的实例的作用域默认是单例。

@Configuration 类级别的注释，表明一个对象是 bean 定义的来源。

@Component @Component在Spring中是元数据，可以用来标注其他的注解。而本身的作用之一就是能够被“component-scanning”扫描到。

@import 注解与XML中的import作用类似

**完全模式和精简模式**

#### 4.使用SpringBoot

依赖中增加了spring-boot-start-web支持Spring MVC，利用@RequestMapping映射到uri来展现bean的信息。

main类增加@SpringBootApplication来自动扫描同类路径(appstarter.bo.*)下配置查找三个Bean。

自动配置的意思：@EnableAutoConfiguration This annotation tells Spring Boot to “guess” how you want to configure Spring, 

based on the jar dependencies that you have added.

# 第二道必做题

根据第一道必做题的第四个实现，计划把“在代码中的映射bean信息到uri上的过程”写成一个starter，然后添加该对starter的依赖。

week5-school-starter业务是：

监听本地端口8080，提供/schoolInfo和/studentInfo两个URI。



开发stater的步骤

1.实现starter要用到的properties类

2.实现starter的autoConfiguration类

3.添加spring.factories文件，指定Starter的自动装配类



项目week5-school依赖了项目week5-school-starter，与第一道必做题的第四个实现的行为一致。

通过starter，项目week5-school不再需要在代码中实现业务功能，只需要添加依赖即可。另外项目week5-school也不需要额外的配置，自动装配了Tomcat和业务配置。项目week5-school不仅仅实现了对业务功能的解耦，而且也免去了配置的步骤。

# 第三道必做题

#### JDBC

表结构：

 `employee (`
  `emp_id smallint(5) unsigned NOT NULL AUTO_INCREMENT,`
  `fname varchar(20) NOT NULL,`
  `lname varchar(20) NOT NULL,`
  `start_date date NOT NULL`

`)`



使用JDBC步骤

1.建立Connection

2.准备statement语句

3.执行statement语句

4.获取ResultSet

5.释放Connection



基于单线程、单连接，在JDBCUtil实现对employee表的增删改查。

接着实现了批处理和开启事务，要留意的是，由于所有SQL都在同一个connection上执行，开启事务之后，后续的SQL都不会自动提交事务。



#### 增加数据库连接池

1.学习hikari-cp的配置

2.代码中增加对连接池的使用

利用了连接池之后，可以使用多线程来执行SQL语句了，不像JDBC中串行执行SQL语句。