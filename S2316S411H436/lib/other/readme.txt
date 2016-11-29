
一、导包说明：
除了junit-4.11.jar和hamcrest-core-1.3.jar，其他jar包都导入到classpath中。
（原因：junit-4.9.jar相当于：junit-4.11.jar和hamcrest-core-1.3.jar的二合一，两种方案选择一种即可。）

二、jar包来源：
此文件夹中的jar包全部来自第三方，一般来自对应官网或者Apache官网。

其中，
（1）如果没有用到JUnit测试，可以从classpath中移除：junit-4.11.jar，hamcrest-core-1.3.jar。
	在较低版本的JUnit4.X中（例如junit-4.9等）是将这两个jar合并在一起的。
（2）如果没有用到DBCP，可以从classpath中移除：commons-dbcp-1.4.jar，commons-pool-1.6.jar。
（3）如果没有用到aspectj，可以从classpath中移除：aspectjweaver-1.8.2.jar。

