# FCT Finder
## Spring boot API to help students and companies get together
<hr>

### Swagger UI endpoint @/api/swagger-ui
### JSON docs endpoint @/docs

<hr>

#### <b>For now, the database is preloaded with Flyway with some companies, and users signing up can add whatever role they want ("admin","mod","user" or any combination as an array in the payload).<b>

<hr>

### Running the API from the IDE:

* #### Create a local database:
  <i>You can use the docker-compose-mysql.yml file</i><br>
  <code>$ cd docker-compose && docker compose -f docker-compose-mysql up -d</code>
  <br>The default values for the database are already setup in "application.properties":
  * URL:
    <code>jdbc:mysql://localhost:53306/fct_finder</code>
  * User: <code>fctfinder</code>
  * Password: <code>fctfinder</code>
  * Database: <code>fct_finder</code>
  * Localhost port: <code>53306</code>
* #### Load the project and run it


<hr>

### Run the API with docker:

* #### Create a local database (same steps as running from the ide:
* #### Edit the docker compose file "docker-compose.yml" to match your database details.

  <i>And run docker compose:</i><br>
    <code>$ cd docker-compose && docker compose up -d</code>

<hr>

### Run the API and the database with docker compose:
* #### Run both the application and a new mysql container as a stack
  <code>$ cd docker-compose && docker compose -f docker-compose-mysql-stack.yml up -d</code>









