# ci-friendly-maven-plugin 

This is the [ci-friendly-maven-plugin](https://github.com/outbrain/ci-friendly-maven-plugin).

[![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/outbrain/ci-friendly-plugin/blob/master/LICENSE) 
[![Build Status](https://travis-ci.org/outbrain/ci-friendly-maven-plugin.svg?branch=main)](https://travis-ci.org/github/outbrain/ci-friendly-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/com.outbrain.swinfra/ci-friendly-maven-plugin.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cci-friendly-maven-plugin)

## Quickstart
This plugin generates a ci-friendly version of your pom.xml and makes maven to install and deploy this one instead of the original pom.xml.
```
   <build>
        <plugins>
          <plugin>
            <groupId>com.outbrain.swinfra</groupId>
            <artifactId>ci-friendly-maven-plugin</artifactId>
            <!--<version>INSERT LATEST VERSION HERE</version>-->
            <configuration>
              <!-- Additional configurations -->
            </configuration>
            <executions>
              <execution>
                <goals>
                  <goal>clean</goal>
                  <goal>ci-friendly</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
   </build>
```
