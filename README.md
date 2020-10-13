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
 values you set by passing them as args. For example, installing version 2.0.0:
 
    mvn -Drevision=2.0.0 clean install
   
 writing the resulting pom to a file named `.ci-friendly-pom.xml` and setting it as the new reactor.
```
   <build>
        <plugins>
          <plugin>
            <groupId>com.outbrain.swinfra</groupId>
            <artifactId>ci-friendly-flatten-maven-plugin</artifactId>
            <!--<version>INSERT LATEST VERSION HERE</version>-->
            <executions>
              <execution>
                <goals>
                  <!-- Ensure proper cleanup. Will run on clean phase-->
                  <goal>clean</goal>
                  <!-- Enable ci-friendly version resolution. Will run on process-resources phase-->
                  <goal>flatten</goal>
                </goals>
              </execution>
            </executions>
          </plugin>
        </plugins>
   </build>
```
## Plugin Goals
 - `ci-friendly-flatten:flatten` replaces `revision`, `sha1`, `changelist`, writes the resolved pom file to `.ci-friendly-pom.xml` and sets it as the new reactor (Default maven phase binding: process-resources).
 - `ci-friendly-flatten:clean` removes any files created by ci-friendly-flatten:ci-friendly (Default maven phase binding: clean).

## Install

1. To avoid having to type `-Drevision=<version>`, define a default revision property. 

         <properties>
            <revision>5.0.0-SNAPSHOT</revision>
         </properties>

`mvn clean install`
Will install all artifacts with 5.0.0-SNAPSHOT version.

2. Provide version with revision arg

`mvn clean install -Drevision=<PROVIDE_VERSION>`

Will install all artifacts with your provided *PROVIDE_VERSION* version

## Deploy

Same as above, just use `mvn clean deploy`

