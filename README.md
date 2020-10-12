# ci-friendly-flatten-maven-plugin 

This is the [ci-friendly-flatten-maven-plugin](https://github.com/outbrain/ci-friendly-flatten-maven-plugin).

[![Apache License V.2](https://img.shields.io/badge/license-Apache%20V.2-blue.svg)](https://github.com/outbrain/ci-friendly-plugin/blob/master/LICENSE) 
[![Build Status](https://travis-ci.org/outbrain/ci-friendly-flatten-maven-plugin.svg?branch=main)](https://travis-ci.org/github/outbrain/ci-friendly-flatten-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/com.outbrain.swinfra/ci-friendly-flatten-maven-plugin.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cci-friendly-flatten-maven-plugin)

## Why another flatten plugin?
Having a large (> 350 modules) reactor with multiple daily releases,
we encountered two issues:
1) Commit history littered with maven release plugin commits.
2) Race between committers pushing to the main branch, and the release plugin pushing its own pom changes.

When we found out about [ci friendly versions](https://maven.apache.org/maven-ci-friendly.html), we were ecstatic - finally we can avoid having
versions in poms and achieve zero commits release process!

The only problem was that the [flatten plugin](https://www.mojohaus.org/flatten-maven-plugin) coupled with the `resolveCiFriendliesOnly`
option [does not work](https://github.com/mojohaus/flatten-maven-plugin/issues/51#issuecomment-566069689).
As adherers of the [unix philosophy](https://en.wikipedia.org/wiki/Unix_philosophy#:~:text=The%20Unix%20philosophy%20emphasizes%20building,as%20opposed%20to%20monolithic%20design.),
we decided to create a plugin that truly, really, only replaces the `revision`, `sha` and `changelist` properties. 

## Quickstart
This plugin flattens a pom by replacing `${revision}`, `${sha1}`, `${changelist}` to 
 values you set by passing them as args e.g.:
 1.If you like to make a version 2.0.0 this can simply being achieved by using this:
 mvn -Drevision=2.0.0 clean package

 2.If you like to make a release with another version:
   mvn -Drevision=2.0.0 -Dchangelist=[FILL_ME] -Dsha1=[FILL_ME]
   
 writing the resulting pom to a file named `.ci-friendly-pom.xml` and instructing maven to use it.
```
   <build>
        <plugins>
          <plugin>
            <groupId>com.outbrain.swinfra</groupId>
            <artifactId>ci-friendly-flatten-maven-plugin</artifactId>
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
