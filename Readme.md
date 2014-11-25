#RText Eclipse plugin

This plugin provides Eclipse support for the ruby textual modeling framework [RText](https://github.com/mthiede/rtext). 

##Installation

Install via update site: `http://mthiede.github.io/rtext_eclipse_plugin/update-site/`.

##Build

The plugins are build using maven and [tycho](https://eclipse.org/tycho/). The update site is located in: `org.rtext.lang.update-site/target/repository`

To perform a full build run: 

```
mvn clean install
```

##Development environment

- [Eclipse Luna](https://eclipse.org/downloads/packages/eclipse-ide-java-developers/lunasr1)
- Latest [Jnario](http://jnario.org) snapshot: `http://www.jnario.org/updates/snapshot/`
