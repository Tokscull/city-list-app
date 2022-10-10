# City List app

# Usage
## backend:
- setup db connection in `application-dev.properties`
- generate ssh key and save into `resorces/keys` directory (`jwt.pem` and `jwt.pub`)
- run application - the db tables will be generated automatically
- run `resorces/data.sql` to generate required roles and initial city information
- create new user using api `api/auth/signup` or via frontend app
- grant `ROLE_ADMID` role to some user via db tool (e.g. DBeaver, intellij db plugin)

## fronted:
- navigate to frontend directory `cd frontend`
- run `npm install`
- run `npm start`
