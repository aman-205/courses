# Course Search API

This project provides a **Course Search API** built with Spring Boot and Elasticsearch.  
It allows users to search and filter courses based on multiple parameters such as keywords, price, age, category, type, and session date.

---

## 📌 Features
- Search courses by keyword (`q`).
- Filter by:
  - **Age range** (`minAge`, `maxAge`)
  - **Category** (`category`)
  - **Type** (`type`)
  - **Price range** (`minPrice`, `maxPrice`)
  - **Next Session Date** (`startDate`)
- Sorting options:
  - `upcoming` → Sort by earliest `nextSessionDate`
  - `priceAsc` → Sort by price (low → high)
  - `priceDesc` → Sort by price (high → low)
- Pagination with `page` and `size` parameters.
- Returns **total number of filtered courses** across all pages.

---

## 📌 Elasticsearch Integration

This project uses **Elasticsearch** as the underlying search engine to index and query course data.  

- Each course is stored as a **document** in the `courses` index.  
- Spring Data Elasticsearch is used to connect Spring Boot with Elasticsearch.  
- A **data loader** (`CourseDataLoader`) loads sample courses from `sample-courses.json` into Elasticsearch on startup.  
- Search queries hit Elasticsearch for filtering, sorting, and pagination.

---

## 📌 Running Elasticsearch with Docker

You can easily spin up Elasticsearch using Docker.

### 1. Run Elasticsearch
```bash
docker run -d \
  --name elasticsearch \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  docker.elastic.co/elasticsearch/elasticsearch:8.9.0
```
---


### 📌 API Endpoint

### Search Courses
- Search courses by keyword (`q`)- GET http://localhost:8080/api/search?q=java.
- Filtered search GET http://localhost:8080/api/search?q=python&minPrice=500&maxPrice=2000&category=Programming&type=Online
- Upcoming courses after today GET http://localhost:8080/api/search?startDate=2025-09-01
- Sorted by price (low to high) GET http://localhost:8080/api/search?sort=priceAsc&page=0&size=5


### Query Parameters
| Parameter   | Type   | Description |
|-------------|--------|-------------|
| `q`         | String | Search keyword in course title |
| `minAge`    | Int    | Minimum age requirement |
| `maxAge`    | Int    | Maximum age requirement |
| `category`  | String | Category of the course (e.g., Programming, Music) |
| `type`      | String | Type of course (Online, Offline, Hybrid) |
| `minPrice`  | Int    | Minimum course price |
| `maxPrice`  | Int    | Maximum course price |
| `startDate` | Date   | ISO-8601 format (e.g., `2025-09-01`) |
| `sort`      | String | `upcoming`, `priceAsc`, `priceDesc` |
| `page`      | Int    | Page number (default: 0) |
| `size`      | Int    | Page size (default: 10) |

---

### 📌 Example Request

GET-http://localhost:8080/api/search?q=java
---

## 📌 Example Response
```json
{
    "courses": [
        {
            "id": "1",
            "title": "Java Basics",
            "category": "Programming",
            "type": "Online",
            "minAge": 16,
            "maxAge": 35,
            "price": 1500.0,
            "nextSessionDate": "2025-09-05"
        },
        {
            "id": "2",
            "title": "Java Advanced",
            "category": "Programming",
            "type": "Online",
            "minAge": 18,
            "maxAge": 40,
            "price": 2000.0,
            "nextSessionDate": "2025-09-10"
        },
        {
            "id": "15",
            "title": "JavaScript Essentials",
            "category": "Programming",
            "type": "Online",
            "minAge": 15,
            "maxAge": 30,
            "price": 1300.0,
            "nextSessionDate": "2025-09-22"
        }
    ],
    "total": 3
}
```
### 📌 How It Works

- Elasticsearch Startup- Docker starts Elasticsearch in single-node mode.

- Course Data Loader- On Spring Boot startup, the CourseDataLoader reads sample-courses.json and indexes all courses into Elasticsearch.

- Search API
/api/search queries Elasticsearch with filters and returns paginated results.
