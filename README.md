# Cumulocity IoT RSS Microservice

This microservice provides events and alarms from Cumulocity IoT as an RSS feed to make them easily consumable by other systems.


## Installation

This microservice is multi-tenant microservice meaning that it can either be installed directly on the tenant for which an RSS should be provided or on the corresponding enterprise or management tenant and then subscribe the child tenant to the microservice. 

The microservice is available as a microservice package from the [Releases](https://github.com/SoftwareAG/cumulocity-rss-microservice/releases) section. The details to install and subscribe a microservice are available in the [Cumulocity IoT Users Guide](https://cumulocity.com/guides/users-guide/administration/#managing-microservices).

## Usage

The microservice provides two endpoints with optional query parameters through which RSS feeds can be fetched:

- Alarms /service/cumulocity-rss-ms/alarms/rss.xml
    - **source** - the device id of a a device
    - **type** - the alarm type
    - **severity** - the alarm severity (one of CRITICAL, MAJOR, MINOR, WARNING)
    - status -the alarm status (one of ACTIVE, ACKNOWLEDGED, CLEARED)
    - **feedSize** - the number of elements to return (limited to a single page with a maximum of 2000 elements in the Alarms API)
- Events: /service/cumulocity-rss-ms/events/rss.xml
    - **source** - the device id of a a device
    - **type** - the event type
    - **feedSize** - the number of elements to return (limited to a single page with a maximum of 2000 elements in the Events API)

------------------------------

This tools are provided as-is and without warranty or support. They do not constitute part of the Software AG product suite. Users are free to use, fork and modify them, subject to the license agreement. While Software AG welcomes contributions, we cannot guarantee to include every contribution in the master project.
_____________________
For more information you can Ask a Question in the [TECHcommunity Forums](https://tech.forums.softwareag.com/tags/c/forum/1/Cumulocity-IoT).

You can find additional information in the [Software AG TECHcommunity](https://tech.forums.softwareag.com/tag/Cumulocity-IoT).
