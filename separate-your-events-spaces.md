---
layout: default
title: Separate your Events - Spaces
---

# Separate your Events - Spaces

Spaces are used to distinguish events from different sources.

It is still possible to compare events stored in different spaces via the web frontend and the RESTful API.


##Authentication Tokens

Authentication tokens are used to ensure some basic authentication for a space, these [tokens must be configured](/configuration-hq-spaces) for each space independently.
Consumers of the RESTful API must supply an authentication token as header parameter in their requests to [store events](/event-api).

