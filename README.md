# SpaceFlightNewsAPI

Crud API developed for the Coodesh Back-end Challenge, accessing a remote ```Heroku``` database, maintained by a batch process running on AWS EC2 instance.

>  This is a challenge by [Coodesh](https://coodesh.com/)

### Technologies

- Java EE 11
- Apache Maven 3.8.1
- Spring Boot 2.6.2
- Spring Framework 2.6.2
- Project Lombok 1.18.22
- Jakarta Validation API 2.0.2
- Google Gson 2.8.9
- ModelMapper 3.0.0
- OpenAPi 3.0
- PostgreSQL
- SQL
- Data Access Object Pattern
- Shell Script
- Crontab
- Heroku 
- AWS EC2
- Unix
- Git & GitHub

### Execution

We have two ways to access the endpoints of the developed API, through the source code, running locally and accessing the remote base,
or directly from the Heroku deploy, through the app URL ```https://space-flights-news-api.herokuapp.com/```.

#### Local run

To run locally, you have that use some ```Java IDE```, or use ```Apache Maven``` to compile the code into executable file, and have access to the source code here,
inside ```space-flight-api``` folder.

<p align="center" id="api-folder">
  <img src="https://user-images.githubusercontent.com/39606289/149602404-d204206b-cbbb-4436-a2c3-68aabab45f52.png" alt="api-folder"/>
</p>

After the source code be at your machine, just open the folder at IDE, and click to run the code!

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149602588-a9be52d3-ac45-4be1-ac09-8dfc90c19de7.png" alt="ide-run"/>
</p>

By accessing ```http://localhost:8080``` and one of the application endpoint, you've been using the API.

You can see at [compilation](###compilation) topic to see how to compile the application, but for run the compiled app, jut type at your terminal
```java -jar SpaceFlightsNews.jar``` to run the app too! But remember, you have to install ```Java``` at your computer and use the command at the same folder 
the executable file is.

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603059-7370edee-6db9-4d38-b94e-c25ed62c5a42.png" alt="terminal-run"/>
</p>

#### Heroku run

The API also is deployed on ```Heroku```, the same website that our database is hosted, to access this method use the application url
```https://space-flights-news-api.herokuapp.com/```.

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603192-3e7e0653-0e47-48b1-a7c0-5ba92d4f9ebf.png" alt="heroku-run"/>
</p>

### Compilation

To compile the source code into the executable application, just type at your terminal inside the API project [folder](#api-folder) the syntax ```mvn clean package```
and your app will be compiled and available at target folder.

### API execution

You can execute the API from your browser, but I advice to use <a href="https://learning.postman.com/docs/getting-started/introduction/" target="_blank">Postman</a> 
to test the API routes.

```GET/``` route that show the follow message if the application is online:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603508-95402608-aab9-4111-b18a-e3ce532eb14e.png" alt="get-route"/>
</p>

```GET/articles``` route returns all the database articles, using pagination feature:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603574-c028879c-2151-4626-8a1d-e825a183a942.png" alt="get-articles-route"/>
</p>

```GET/articles/{id}``` route that returns a specific article based on id:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603631-b4b5ef64-ced0-4dd1-9b28-2139492f5d4a.png" alt="get-articles-id-route"/>
</p>

```POST/articles``` route that add a new article to our database:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603685-d9890797-807f-4892-9b90-d99995a89302.png" alt="post-articles-route"/>
</p>

```PUT/articles/{id}``` route that updates an specific article to our database:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603807-bae3145f-88f0-4b4f-af4b-519df3991013.png" alt="put-articles-route"/>
</p>

```DELETE/articles/{id}``` route that delete an specific article from our database, be carefull when delete articles:

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603869-0cb6e8e0-d272-476e-a12a-2cba17506ff9.png" alt="delete-articles-route"/>
</p>

To see what you need to use when call the api endpoints, take a time to look at our live documentation ```https://space-flights-news-api.herokuapp.com/swagger-ui/index.html#/```

Disclaimer: the url at the postman app is an environment variable defined by me and pointing to the API running on Heroku.

<p align="center">
  <img src="https://user-images.githubusercontent.com/39606289/149603971-b9c0db07-1cda-478f-b344-15e69b019f95.png" alt="postman-variable"/>
</p>
