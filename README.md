### LocaWork

# Run: 
```mvn spring-boot:run```

#Connect to db:
```heroku pg:psql postgresql-pointy-79328 --app locawork-web-api```

##Adding Posgre SQL extensions:
```CREATE EXTENSION earthdistance;```
```CREATE EXTENSION cube;```
```CREATE EXTENSION pgcrypto;```

#Update or create with Flyway
### change application.properties
```spring.jpa.hibernate.ddl-auto=create```

#View server logs
```heroku logs --tail -a locawork-web-api```

#Endpoints
###Getting all jobs
```https://locawork-web-api.herokuapp.com/jobs/```
### Updating job
Updating job with id 4<br>
``` https://locawork-web-api.herokuapp.com/jobs/4```
```
{
	"id": 4,
	"title": "Muru niitmine",
	"description" : "Tule niida aias muru. Kokku on 100 ruutmeetrit.",
	"salary" : 100,
	"longitude": "58.698017",
	"latitude": "-152.522067"
}
```

### Deleting job
Deleting job with id 4 <br>
``` https://futumap.herokuapp.com/jobs/4```
###Gettin jobs by location in a distance
Gettin jobs by location in a distance. Distance is in METERS! Add your longitude and latitude after '='.
Example with Longitude 152.522067 and latitude 58.698017<br>
googleAccountId is currently logged in user to filter out account from finding job.

```https://locawork.herokuapp.com/jobs/getjobsbylocation?longitude=-152.522067&latitude=58.698017&distance=10000000&googleAccountId=108921010361347349816```




