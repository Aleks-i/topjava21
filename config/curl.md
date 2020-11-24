#### curl for test

####        Users

####Get All Users
curl -s http://localhost:8081/topjava/rest/admin/users

####Get User 100000
curl -s http://localhost:8081/topjava/rest/admin/users/100000

#### Delete Users 100001
curl -s -X DELETE http://localhost:8081/topjava/rest/admin/users/100001

#### Get User By Email user@yandex.ru
curl -s http://localhost:8081/topjava/rest/admin/users/by?email=user@yandex.ru

####        Meals

#### Get Meal 100002
curl -s http://localhost:8081/topjava/rest/profile/meals/100002

#### Delete Meals 100003
curl -s -X DELETE http://localhost:8081/topjava/rest/profile/meals/100003

#### Get All Meals
curl -s http://localhost:8081/topjava/rest/profile/meals

#### Create Meal
curl -s -X POST -d '{"dateTime":"2020-11-24T15:00", "description":"новый обед", "calories":1000}' -H 'Content-Type: application/json' http://localhost:8081/topjava/rest/profile/meals

#### Update Meal 100002
curl -s -X PUT -d '{"dateTime":"2020-11-24T14:00", "description":"обновленный обед", "calories":1000}' -H 'Content-Type: application/json' http://localhost:8081/topjava/rest/profile/meals/100002

#### Get Filter Meal
curl -s "http://localhost:8081/topjava/rest/profile/meals/filter?startDateTime=2020-01-31T00:00:00&endDateTime=2020-01-31T23:59:59"