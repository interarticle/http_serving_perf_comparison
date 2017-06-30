// This is the simplest implementation I came up with in golang. You could
// argue the use of atomic.AddUint64 is a performance optimization - however, I
// would argue that is the simplest solution; using mutexes is more lines of
// code.
//
// Build with `go build`.
// Run with sudo ulimit -n 20000 to ensure you don't run out of sockets in a
// parallel connection test.
package main

import (
	"fmt"
	"log"
	"net/http"
	"sync/atomic"
	"time"
)

var requestCounter int64 = 0

func formatResponse() string {
	return fmt.Sprintf(
		"Speed Test Response\n"+
			"This is request #%06d\n"+
			"Current time is %s",
		atomic.AddInt64(&requestCounter, 1),
		time.Now().UTC().Format(time.RFC3339))
}

func main() {
	http.HandleFunc("/perf", func(w http.ResponseWriter, r *http.Request) {
		_, err := w.Write([]byte(formatResponse()))
		if err != nil {
			log.Print(err)
		}
	})
	log.Fatal(http.ListenAndServe(":8080", nil))
}
