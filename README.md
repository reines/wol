WOL
========

A simple Wake-On-LAN implementation.

[![Build Status](https://api.travis-ci.org/reines/wol.png)](https://travis-ci.org/reines/wol)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.jamierf/wol/badge.png)](https://maven-badges.herokuapp.com/maven-central/com.jamierf/wol)

WOL can be found in maven central.

## Installation

```xml
<dependency>
    <groupId>com.jamierf</groupId>
    <artifactId>wol</artifactId>
    <version>...</version>
</dependency>
```

## Usage

```java
WakeOnLan.wake("b8:f6:b1:14:6a:b7");
```
