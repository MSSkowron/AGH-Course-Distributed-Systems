# RabbitMQ - Agencies & Carriers

- **Running RabbitMQ**

  To run RabbitMQ, execute the following command:

  ```
  docker compose up
  ```

- **Running an agency**

  To run an agency, navigate to the project directory and execute the following command:

  ```
  go run . --agency "agency-name" --jobs "job-name1,job-name2,job-name3" --numberOfJobs "number-of-jobs"
  ```

  Replace:

  - "agency-name" with the desired name for the agency
  - "job-name1,job-name2,job-name3" with a comma-separated list of job names
  - "number-of-jobs" with the desired number of jobs

- **Run a carrier**

  To run an agency, navigate to the project directory and execute the following command:

  ```
  go run . --carrier "carrier-name" --jobs "job_name1,job_name2,job_name3"
  ```

  Replace:

  - "carrier-name" with the desired name for the carrier
  - "job_name1,job_name2,job_name3" with a comma-separated list of job names

- **Example**

  Run every command in a different command line:

  ```
  go run . --agency "agency1" --jobs "person_transport,cargo_transport,satellite_transport" --numberOfJobs 10
  go run . --agency "agency2" --jobs "person_transport,cargo_transport,satellite_transport" --numberOfJobs 10
  go run . --carrier "carrier1" --jobs "person_transport,cargo_transport"
  go run . --carrier "carrier2" --jobs "cargo_transport,satellite_transport"
  ```
