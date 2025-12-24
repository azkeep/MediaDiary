package main

import (
	"database/sql"
	"fmt"
	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"log"
	"net/http"
	"os"
)

type StatsResponse struct {
	Title       string `json:"title"`
	Last3days   int    `json:"last_3_days"`
	Last7days   int    `json:"last_7_days"`
	Last30days  int    `json:"last_30_days"`
	Last180days int    `json:"last_180_days"`
	Total       int    `json:"total"`
}

var db *sql.DB

func loadEnv() {
	err := godotenv.Load(".env", "../.env")
	if err != nil {
		log.Println("Note: No .env file found in local or parent directory")
	}
}

// Helper function to read environment variables with a fallback
func getEnv(key, fallback string) string {
	if value, ok := os.LookupEnv(key); ok {
		return value
	}
	return fallback
}

func initDB() {
	dbHost := getEnv("DB_HOST", "localhost")
	dbUser := getEnv("DB_USER", "postgres")
	dbPass := getEnv("DB_PASSWORD", "password")
	dbName := getEnv("DB_NAME", "media_diary")
	dbPort := getEnv("PORT_DB", "5432")

	connStr := fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable",
		dbHost, dbPort, dbUser, dbPass, dbName)

	var err error
	db, err = sql.Open("postgres", connStr)
	if err != nil {
		panic(err)
	}
}

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

func main() {
	loadEnv()

	initDB()

	var err error
	err = db.Ping()
	if err != nil {
		panic("Database unreachable: " + err.Error())
	}

	router := gin.Default()

	allowedOrigin := getEnv("ALLOWED_ORIGIN", "http://localhost:3000")
	router.Use(func(c *gin.Context) {
		c.Writer.Header().Set("Access-Control-Allow-Origin", allowedOrigin)
		c.Writer.Header().Set("Access-Control-Allow-Methods", "GET, OPTIONS")
		c.Writer.Header().Set("Access-Control-Allow-Headers", "Origin, Content-Type")

		if c.Request.Method == "OPTIONS" {
			c.AbortWithStatus(204)
			return
		}
		c.Next()
	})

	router.GET("/stats", getTitleStats)

	goPort := getEnv("PORT_GO", "8181")
	log.Printf("Go Stats Service starting on port %s", goPort)

	err = router.Run(":" + goPort)
	if err != nil {
		panic(err)
	}
}
