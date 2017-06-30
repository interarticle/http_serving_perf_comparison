# Test Environment

CPU: Intel(R) Core(TM) i7-3630QM CPU @ 2.40GHz
RAM Total: 24578628k

# Test Methodology

Under `ulimit -n 20000`, running the server using

```
go build golang_http_serve.go
/usr/bin/time ./golang_http_serve
```

with Go version

```
go version go1.8.3 linux/amd64
```

Test using:

```
ab -c 10000 -n 20000 http://127.0.0.1:8080/perf
```

The above command is run once to prime the Go program (caches etc). The second
run of the command is used to produce the test results. Note: memory
consumption during the first run still counts towards maxresident.

# Test Results

`ab` outputs:

```
This is ApacheBench, Version 2.3 <$Revision: 1757674 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 2000 requests
Completed 4000 requests
Completed 6000 requests
Completed 8000 requests
Completed 10000 requests
Completed 12000 requests
Completed 14000 requests
Completed 16000 requests
Completed 18000 requests
Completed 20000 requests
Finished 20000 requests


Server Software:        
Server Hostname:        127.0.0.1
Server Port:            8080

Document Path:          /perf
Document Length:        80 bytes

Concurrency Level:      10000
Time taken for tests:   1.344 seconds
Complete requests:      20000
Failed requests:        0
Total transferred:      3940000 bytes
HTML transferred:       1600000 bytes
Requests per second:    14876.96 [#/sec] (mean)
Time per request:       672.180 [ms] (mean)
Time per request:       0.067 [ms] (mean, across all concurrent requests)
Transfer rate:          2862.07 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:       96  171  42.5    169     260
Processing:    33  176  53.8    182     282
Waiting:       18   70  51.7     60     255
Total:        266  347  20.5    351     450

Percentage of the requests served within a certain time (ms)
  50%    351
  66%    357
  75%    358
  80%    360
  90%    362
  95%    367
  98%    374
  99%    396
 100%    450 (longest request)
```

`time` outputs:

```
3.22user 2.24system ...elapsed ...%CPU (0avgtext+0avgdata 143804maxresident)k
0inputs+0outputs (0major+36575minor)pagefaults 0swaps
```
