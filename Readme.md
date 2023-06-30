# FCT Finder
## Spring boot API to help students and companies get together
<hr>

### Swagger UI endpoint @/api/swagger-ui
### JSON docs endpoint @/docs

<hr>

### Running the API from the IDE:

* #### Create a local database:
  * <i>You can run a full mysql installation or use docker/docker compose</i>
    <br>Docker run example:
    <br>$ docker run --name mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -e MYSQL_DATABASE=fctfinder
* #### Load the project
* #### Change the profile in 'application.properties' to 'local'
* #### Edit the details of your local mysql database into 'application.properties'

<hr>

### Run the API with docker:

  * Run "docker compose up" from this project root folder

<hr>

### Run the API and the database with docker compose:

  * Run "docker compose -f docker-compose-with-mysql.yml up" from this project root folder

<hr>

# Important:
## The 'roles' table in the database has to be populated using the queries in the data-insert.sql file manually.
## It could be loaded automatically changing the name to data.sql, but then application won't run on subsequent starts
## Will look into flyway/liquidbase to automate it.






