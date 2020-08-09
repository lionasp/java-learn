# web-quiz-engine
Task https://hyperskill.org/projects/91

A simple REST service for creating and solving quizzes through HTTP API.

It uses an embedded H2 database to store all data in the file system.

This project is my first try with https://spring.io/ framework and gradle https://gradle.org/

## Endpoints

### Registration

```
curl -X POST -H "Content-Type: application/json" http://localhost:8889/api/register \
--data '{"email":"hello@world.com", "password": "12345"}'
```

Email must be unique, password must contain at least 5 chars

### Add a quiz

```
curl --user hello@world.com:12345 -X POST -H "Content-Type: application/json" \
-d '{"title":"The Java Logo", "text":"What is depicted on the Java logo?", "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"], "answer": [2]}' \
http://localhost:8889/api/quizzes
```


### Delete a quiz

```
curl --user hello@world.com:12345 -X DELETE  http://localhost:8889/api/quizzes/1
```

### Get a quiz

```
curl --user hello@world.com:12345 -X GET http://localhost:8889/api/quizzes/1
```

### Get all quizzes

```
curl --user hello@world.com:12345 -X GET http://localhost:8889/api/quizzes
```

### Solve a quiz

```
curl --user hello@world.com:12345 -X POST -H 'Content-Type: application/json' \
http://localhost:8889/api/quizzes/1/solve --data '[1, 2]'
```

### Get user's completed quizzes

```
curl --user hello@world.com:12345 -X GET  http://localhost:8889/api/quizzes/completed
```