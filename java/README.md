# Test Environment

CPU: Intel(R) Core(TM) i7-3630QM CPU @ 2.40GHz
RAM Total: 24578628k

# Test Methodology

Under `ulimit -n 20000`, running the server using

```
/usr/bin/time java -jar target/perfTest-1.0-SNAPSHOT-jar-with-dependencies.jar
```

with Java version

```
openjdk version "1.8.0_131"
OpenJDK Runtime Environment (build 1.8.0_131-b11)
OpenJDK 64-Bit Server VM (build 25.131-b11, mixed mode)
```

Test using:

```
ab -c 10000 -n 20000 http://127.0.0.1:8080/perf
```

The above command is run once to prime the JVM and trigger JIT compilation. The
second run of the command is used to produce the test results. Note: memory
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
Document Length:        84 bytes

Concurrency Level:      10000
Time taken for tests:   14.714 seconds
Complete requests:      20000
Failed requests:        1021
   (Connect: 0, Receive: 0, Length: 1021, Exceptions: 0)
Total transferred:      3890695 bytes
HTML transferred:       1594236 bytes
Requests per second:    1359.28 [#/sec] (mean)
Time per request:       7356.829 [ms] (mean)
Time per request:       0.736 [ms] (mean, across all concurrent requests)
Transfer rate:          258.23 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0  161 360.4      1    1042
Processing:     1  729 2250.2      5   13674
Waiting:        0  334 1449.7      5   13674
Total:          2  890 2369.4      6   14700

Percentage of the requests served within a certain time (ms)
  50%      6
  66%     16
  75%    198
  80%    284
  90%   2745
  95%   6023
  98%   9119
  99%  13165
 100%  14700 (longest request)
```

`time` outputs:

```
13.25user 6.52system ...elapsed ...CPU (0avgtext+0avgdata 1769220maxresident)k
0inputs+0outputs (0major+541845minor)pagefaults 0swaps
```
