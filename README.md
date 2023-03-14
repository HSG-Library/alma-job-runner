# Alma Job Runner, schedule any manual Alma Job!
This app allows schedule any Alma job. The app loads job configurations from another Git repository and
registers a cron job for every configuration file.
When the cron expressions triggers, the job is started via an Alma API call.

# How to configure and run the app
## App Configuration
The app needs three environment variables to run:

* `GIT_REPO_SSH`: this is the connection to the Git repo with the job config files (more on this later), something like `git@github.com:HSG-Library/alma-job-runner-config.git`
* `GIT_PRIVATE_KEY_BASE64`: this is the private SSH key encoded with Base64, needed to authenticate when cloning the config repo (more on this later), something like `"LS0tLS1CRUdJTiBPUEVOU1NIIFBSSVZBVEUgS0VZLS0tLS0KYjN
CbGJuTnphQzFyWlhrdGRqRUFBQUFBQkc1dmJtVUFBQUFFYm05dVpRQUFBQUFBQUFBQkFBQUFNd0FBQUF0emMyZ3RaVwpReU5UVXhPUUFBQUNBSTFGMzdNdWJrRWRoT20yYS"`. The Base64 encoding is needed to avoid issues with newlines when passing environment variables to Docker.
* `ALMA_API_KEY`: this is the Alma API key needed to start the jobs. The key needs write permission to the 'Configuration' area.
* `ALMA_API_URL`: this is the base URL for the Alma API (e.g. `https://api-eu.hosted.exlibrisgroup.com/`)

## Run the app
The app is available as Docker image.

Run the Docker container, [Docker](https://www.docker.com/) must be installed:
```bash
export GIT_REPO_SSH="git@github.com:your/config-repo.git"
export GIT_PRIVATE_KEY_BASE64="<base64-of-your-private-key>"
export ALMA_API_KEY="your-alma-api-key"
export ALMA_API_URL="https://api-eu.hosted.exlibrisgroup.com/"

docker run -i -p 8080:8080 --env GIT_REPO_SSH=$GIT_REPO_SSH --env GIT_PRIVATE_KEY_BASE64=$GIT_PRIVATE_KEY_BASE64 --env ALMA_API_KEY=$ALMA_API_KEY ghcr.io/hsg-library/alma-job-runner
```
Since Docker can not handle `\n` in `--env` parameters, we need to encode the private key with base64. Encode the key like this on Linux-like or macOS:
```bash
cat path/to/your/privatekey | base64
```
Encode like this on Windows in PowerShell:
```powershell
([Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("-----BEGIN OPENSSH PRIVATE KEY-----
b3BlbnNzaC1rZXktdjEAAAAABG5vbmUAAAAEbm9uZQAAAAAAAAABAAAAMwAAAAtzc2gtZW
[...your-full-private-key]
Fj2Nw1+DxfGbAfJJvs7NAAAACWFsbWEtam9icwECAwQ=
-----END OPENSSH PRIVATE KEY-----
"))) -join ""
```
The `job` endpoints should now be available under `http://localhost:8080/jobs`. The following endpoints are available:
- register: register all job configs from the git repo `http://localhost:8080/jobs/register`
- unregister: unregister all job configs from the git repo `http://localhost:8080/jobs/unregister`
- reregister: first unregister all jobs, get a new clone of the config repo and register all job configs `http://localhost:8080/jobs/reregister`
- list: list all registered jobs `http://localhost:8080/jobs/list`
- results: show the responses from the run jobs since the start of the instance `http://localhost:8080/jobs/results`

# How to configure the jobs and where store the config files
## Job configuration files format
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
## Where to get the configuration data
* Name: just choose a fitting name
* Cron expression: this defines when the job should run, use websites like https://crontab.guru or https://cronexpressiontogo.com
* Method and Api URI: Use Alma. This information is provided when configuring a job. See: https://developers.exlibrisgroup.com/blog/working-with-the-alma-jobs-api/
* XML Payload: Same as above

## How to store the config files
The config files must be stored in another Git repo. This repo should probably be private.
The files should be in the main branch without any directories.

It must be possible to clone the repo via SSH. To achieve this the following steps are necessary:
1. Generate a key pair, see e.g. https://www.purdue.edu/science/scienceit/ssh-keys-windows.html
2. Add your public key as deployment key in your git repo
3. Keep your private key secret, but configure it as environment variable to run the app

# Dev-Notes
## Run in VSCode
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
        "GIT_PRIVATE_KEY_BASE64": "<base64 of your private key>",
        "ALMA_API_KEY": "YOUR-ALMA-API-KEY",
        "ALMA_API_URL": "https://api-eu.hosted.exlibrisgroup.com/",
    }
}
```

## Build
Build Docker image:
```bash
./mvnw package
```
Push Docker image:<br>
This needs authentication and is done via GitHub Workflow, using the GitHub Token from the workflow. If the image must be pushed otherwise, a GitHub PAT must be used.
```bash
./mvnw deploy
```
## Test
Test don't run automatically via Maven, because packaging is set to `docker`. To run tests via Maven use the `jar` profile:
```bash
./mvnw test -Pjar
```

## Upgrade dependencies
**Update Micronaut**<br>
* Check the current version of Micronaut: [https://micronaut.io/download/](https://micronaut.io/download/)
* Set the current Version in the `pom.xml` file in `parent > version` and in `properties > micronaut.version`

**Update other dependencies**<br>
Make sure, the versions of the dependencies to be updates are maintained in properties (`pom.xml`)
* Check for updates, but only in versions, managed in the properties:

		mvn versions:display-property-updates

* update properties:

		mvn versions:update-properties

## Additional resources
https://developers.exlibrisgroup.com/blog/working-with-the-alma-jobs-api/
https://developers.exlibrisgroup.com/alma/apis/docs/conf/UE9TVCAvYWxtYXdzL3YxL2NvbmYvam9icy97am9iX2lkfQ==/

## Micronaut CLI
Generated project with the following Micronaut CLI command:

    mn create-app --build=maven --jdk=17 --lang=java --test=junit --features=logback ch.unisg.library.systemlibrarian.alma-job-runner
