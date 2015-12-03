---
layout: default
title: Configuration HQ - Spaces
---

#Spaces

When uploading *Events* to the *Logspace HQ* or downloading *[Orders](/configuration-hq-orders)*, the caller must state in which *[Space](/separating-events#spaces)* the operation is taking place. But instead of stating the *Space* directly, a *Space-Token* has to be provided. This approach allows more fine grained control and remapping of callers to different *Spaces* without having to reconfigure the caller themselves.

Each *Space* is stored as a single file in the ``spaces`` sub-directory inside the [data directory](/configuration-hq-system-properties#data-directory). The name of the *Space* is used as the name of file, with the extension ".space".<br/>
So the *Space* called ``development`` is stored as the file `<data-directory>/spaces/development.space`.

The *Space-Tokens* are stored as individual lines in the file of the *Space*.<br/>
So to assign the *Space-Tokens* ``279a15ce-9258-4964-9fe8-5e1d89b5a4eb`` and ``013bc013-4f69-4cb6-b1c2-2a2f4ba3b377`` to the *Space* ``development`` they have to be added to the file's content:

```
# This is a comment
#
# These are the Space-Tokens for this Space 
279a15ce-9258-4964-9fe8-5e1d89b5a4eb
013bc013-4f69-4cb6-b1c2-2a2f4ba3b377

# the next Space-Token is commented out and cannot be used
# 01b7139c-e0fa-49bc-a1d0-9420f0ea9edc
``` 

>Changes to *Spaces* or *Space-Tokens* will only apply after the *Logspace HQ* has been restarted.<br/>
>A *Space-Token* cannot be assigned to more than one *Space*. The *Logspace HQ* will fail to start when a misconfiguration is encountered. 