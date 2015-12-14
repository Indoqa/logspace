---
layout: default
title: Setting Up Solr
---

# Setting up Solr

The *Logspace HQ* uses Apache Solr 5.3.1 as *Event Store*.

Installing Solr and creating the required core is very easy and takes only a few minutes:

1. Download [Apache Solr 5.3.1](http://archive.apache.org/dist/lucene/solr/5.3.1/)
2. Extract the archive into a directory of your choosing.
3. Start your Solr instance by executing 
```
bin/solr start
```
in that directory.
4. You can check the new Solr instance by pointing your browser at <a href="http://localhost:8983" target="_blank">localhost:8983</a><br/>
![Empty Solr Instance](/assets/images/setting-up-solr-instance.png)
5. Download the Logspace Solr schema from [Github](https://github.com/Indoqa/logspace/tree/logspace-{{site.data.logspace.version}}/logspace-hq-solr-plugin/src/main/resources/META-INF/solr/logspace/conf/) or extract it from the [distribution](/download)
5. Create a new Solr core using the directory you placed the Logspace Solr schema in
```
bin/solr create -c logspace -d path/to/solr-schema
```
6. The new core will be available immediately:<br/>
![Logspace Core](/assets/images/setting-up-solr-core.png)

You can now configure your *Logspace HQ* with the URL ```http://localhost:8983/solr/logspace```.


For more information about configuring Solr take a look at the <a href="http://lucene.apache.org/solr/resources.html" target="_blank">Solr online documentation</a>.