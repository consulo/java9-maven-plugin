# Java 9 module generator - maven plugin

## About

This plugin will helps migration from Java 8 to Java 9. It will generate module-info.class file while Java 8 compilation, which will be usable while Java 9 compilation. At Java 8 file will be ignored

## Usage example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
		 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>consulo.internal</groupId>
	<artifactId>untitled4</artifactId>
	<version>1.0</version>

	<repositories>
		<repository>
			<id>consulo</id>
			<url>https://maven.consulo.io/repository/snapshots/</url>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
	</repositories>

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.7.0</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>

			<plugin>
				<groupId>consulo.internal</groupId>
				<artifactId>java9-maven-plugin</artifactId>
				<version>1.0-SNAPSHOT</version>
				<extensions>true</extensions>
				<configuration>
					<module>
						<open>true</open>
						<name>untitled</name>
						<requires>
							<require>
								<name>org.objectweb.asm</name>
								<transitive>true</transitive>
							</require>
						</requires>
					</module>
				</configuration>
				<executions>
					<execution>
						<id>java9</id>
						<phase>generate-sources</phase>
						<goals>
							<goal>generate-source-module-info</goal>
						</goals>
					</execution>
					<execution>
						<id>java8</id>
						<phase>process-classes</phase>
						<goals>
							<goal>generate-binary-module-info</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>

	<dependencies>
		<dependency>
			<groupId>org.ow2.asm</groupId>
			<artifactId>asm</artifactId>
			<version>6.2</version>
		</dependency>
	</dependencies>
</project>
```