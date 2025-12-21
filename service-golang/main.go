package main

import (
	"database/sql"
	"github.com/gin-gonic/gin"
	_ "github.com/lib/pq"
	"net/http"
)

type StatsResponse struct {
	Title       string `json:"title"`
	Last3days   int    `json:"last_3_days"`
	Last7days   int    `json:"last_7_days"`
	Last30days  int    `json:"last_30_days"`
	Last180days int    `json:"last_180_days"`
	Total       int    `json:"total"`
}

//type Last3DaysResponse struct {
//	Title       string `json:"title"`
//	Last3days   int    `json:"last_3_days"`
//	Last7days   int    `json:"last_7_days"`
//	Last30days  int    `json:"last_30_days"`
//	Last180days int    `json:"last_180_days"`
//	Total       int    `json:"total"`
//}

var db *sql.DB

func getTitleStats(context *gin.Context) {
	title := context.Query("title")
	if title == "" {
		context.JSON(http.StatusBadRequest, gin.H{"error": "Title is required"})
		return
	}

	var statsResponse StatsResponse
	statsResponse.Title = title

	var exists bool
	query := `
		SELECT
		    EXISTS(SELECT 1 FROM media_selected WHERE title = $1), 
			COUNT(*) FILTER	(WHERE date_selected > NOW() - INTERVAL '3 days'),
			COUNT(*) FILTER	(WHERE date_selected > NOW() - INTERVAL '7 days'),
			COUNT(*) FILTER	(WHERE date_selected > NOW() - INTERVAL '30 days'),
			COUNT(*) FILTER	(WHERE date_selected > NOW() - INTERVAL '180 days'),
			COUNT(*) 
		FROM media_selected
		WHERE title = $1`

	err := db.QueryRow(query, title).Scan(
		&exists,
		&statsResponse.Last3days,
		&statsResponse.Last7days,
		&statsResponse.Last30days,
		&statsResponse.Last180days,
		&statsResponse.Total)

	if err != nil {
		context.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	if !exists {
		context.JSON(http.StatusNotFound, gin.H{"message": "No data found for the given title"})
		return
	}

	context.JSON(http.StatusOK, statsResponse)
}

func get3DaysStats(context *gin.Context) {

	context.JSON(http.StatusOK, nil)
}

func main() {
	var err error
	connStr := "host=localhost port=5432 user=postgres password=postgres dbname=matalogue sslmode=disable"
	db, err = sql.Open("postgres", connStr)
	if err != nil {
		panic(err)
	}

	err = db.Ping()
	if err != nil {
		panic("Database unreachable: " + err.Error())
	}

	router := gin.Default()

	router.Use(func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", "http://localhost:3000")
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Origin, Content-Type")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204)
			return
		}
		c.Next()
	})

	router.GET("/stats", getTitleStats)

	err = router.Run(":8181")
	if err != nil {
		panic(err)
	}
}
