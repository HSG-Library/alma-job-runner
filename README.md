# Alma Job Runner, schedule any manual Alma Job!
This app allows schedule any Alma job. The app loads job configurations from another Git repository and
registers a cron job for every configuration file.
When the cron expressions triggers, the job is started via an Alma API call.

## How to configure and run the app
### App Configuration
The app needs three environment variables to run:

* `GIT_REPO_SSH`: this is the connection to the Git repo with the job config files (more on this later), something like `git@github.com:HSG-Library/alma-job-runner-config.git`
* `GIT_PRIVATE_KEY`: this is the private SSH key, needed to authenticate when cloning the config repo (more on this later), something like `"-----BEGIN OPENSSH PRIVATE KEY-----\nb3NOTMYACTUALKEYrZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW\nQyNTUxOQAAACAI1F[...]`. Notice the '\n' in the key.
* `ALMA_API_KEY`: this is the Alma API key needed to start the jobs. The key needs write permission to the 'Configuration' area.

### Run the app
The app is available as runnable jar and as Docker container.

Run the jar (environment variables must be set):
```
TODO: WRITE COMMAND
```

Run the Docker container (environment variables set when starting the container):
```
TODO: WRITE COMMAND
```
## How to configure the jobs and where store the config files
### Job configuration files format
Filename: `[any-name].conf` (files without the `.conf` extension will be ignored)

* 1st line: name of the Job
* 2nd line: cron expression
* 3rd line: HTTP method and API URI
* rest: XML payload

Example:
```
Add titles to collection

45 23 * * *

POST /almaws/v1/conf/jobs/M502xxx?op=run

<job>
	<parameters>
		<parameter>
			<name>COLLECTION_NAME</name>
			<value>Test-Collection</value>
		</parameter>
		<parameter>
			<name>UNASSIGN_FROM_COLLECTION</name>
			<value>false</value>
		</parameter>
		<parameter>
			<name>COLLECTION_ID</name>
			<value>81702xxx</value>
		</parameter>
		<parameter>
			<name>set_id</name>
			<value>33624xxx</value>
		</parameter>
		<parameter>
			<name>job_name</name>
			<value>Add Titles to Collection - via API - jfu-test</value>
		</parameter>
	</parameters>
</job>
```
### Where to get the configuration data
* Name: just choose an fitting name
* Cron expression: this defines when the job should run, use websites like https://crontab.guru or https://cronexpressiontogo.com
* Method and Api URI: Use Alma. This information is provided when configuring a job. See: https://developers.exlibrisgroup.com/blog/working-with-the-alma-jobs-api/
* XML Payload: Same as above

### How to store the config files
The config files must be stored in another Git repo. This repo should probably be private.
The files should be in the main branch without any directories.

It must be possible to clone the repo via SSH. To achieve this the following steps are necessary:
1. Generate a key pair, see e.g. https://www.purdue.edu/science/scienceit/ssh-keys-windows.html
2. Add your public key as deployment key in your git repo
3. Keep your private key secret, but configure it as environment variable to run the app




## Dev-Notes
### Run in VSCode
Add the following command to your `launch.json`:
```
{
    "type": "java",
    "name": "Run",
    "request": "launch",
    "mainClass": "ch.unisg.library.systemlibrarian.Application",
    "projectName": "alma-job-runner",
    "env": {
        "GIT_REPO_SSH": "git@github.com:your/config-repo",
        "GIT_PRIVATE_KEY": "-----BEGIN OPENSSH PRIVATE KEY-----\nYOUR-PRIVATE-KEY\n-----END OPENSSH PRIVATE KEY-----",
        "ALMA_API_KEY": "YOUR-ALMA-API-KEY"
    }
}
```

### Build
Runnable jar:
```
TODO: ADD COMMAND
```
Docker container:
```
TODO: ADD COMMAND
```

### Upgrade dependecies
**Update Micronaut**<br>
* Check the current version of Micronaut: [https://micronaut.io/download/](https://micronaut.io/download/)
* Set the current Version in the `pom.xml` file in `parent > version` and in `properties > micronaut.version`

**Update other dependencies**<br>
Make sure, the versions of the dependencies to be updates are maintained in properties (`pom.xml`)
* Check for updates, but only in versions, managed in the properties:

		mvn versions:display-property-updates

* update properties:

		mvn versions:update-properties

### Additional resources
https://developers.exlibrisgroup.com/blog/working-with-the-alma-jobs-api/
https://developers.exlibrisgroup.com/alma/apis/docs/conf/UE9TVCAvYWxtYXdzL3YxL2NvbmYvam9icy97am9iX2lkfQ==/

### Micronaut CLI
Generated project with the following Micronaut CLI command:

    mn create-app --build=maven --jdk=17 --lang=java --test=junit --features=logback ch.unisg.library.systemlibrarian.alma-job-runner
