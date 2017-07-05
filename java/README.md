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

# Notes

The first run of `ab` fails with a timeout:

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
apr_pollset_poll: The timeout specified has expired (70007)
Total of 19640 requests completed
```

# A tiny small work
Compare with NanoHttpd

```
This is ApacheBench, Version 2.3 <$Revision: 1796539 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:        Java
Server Hostname:        127.0.0.1
Server Port:            8080

Document Path:          /perf
Document Length:        88 bytes

Concurrency Level:      10000
Time taken for tests:   1.413 seconds
Complete requests:      10000
Failed requests:        0
Total transferred:      2000000 bytes
HTML transferred:       880000 bytes
Requests per second:    7076.62 [#/sec] (mean)
Time per request:       1413.103 [ms] (mean)
Time per request:       0.141 [ms] (mean, across all concurrent requests)
Transfer rate:          1382.15 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0   12 105.0      0    1004
Processing:     0    7  29.0      4     408
Waiting:        0    7  29.0      3     408
Total:          1   20 130.6      4    1411

Percentage of the requests served within a certain time (ms)
  50%      4
  66%      5
  75%      6
  80%      6
  90%      9
  95%     11
  98%    109
  99%   1007
 100%   1411 (longest request)
```
Little improvement using NIO

```
/usr/bin/time java -cp target/perfTest-1.0-SNAPSHOT-jar-with-dependencies.jar com.example.perf.SimpleHttp
```

```

This is ApacheBench, Version 2.3 <$Revision: 1796539 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking 127.0.0.1 (be patient)
Completed 1000 requests
Completed 2000 requests
Completed 3000 requests
Completed 4000 requests
Completed 5000 requests
Completed 6000 requests
Completed 7000 requests
Completed 8000 requests
Completed 9000 requests
Completed 10000 requests
Finished 10000 requests


Server Software:        
Server Hostname:        127.0.0.1
Server Port:            8080

Document Path:          /perf
Document Length:        84 bytes

Concurrency Level:      10000
Time taken for tests:   53.759 seconds
Complete requests:      10000
Failed requests:        552
   (Connect: 0, Receive: 0, Length: 552, Exceptions: 0)
Total transferred:      1927392 bytes
HTML transferred:       793632 bytes
Requests per second:    186.02 [#/sec] (mean)
Time per request:       53758.623 [ms] (mean)
Time per request:       5.376 [ms] (mean, across all concurrent requests)
Transfer rate:          35.01 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0  182 388.8      0    1032
Processing:     1 1990 5851.9     16   52693
Waiting:        0 1258 4634.6     15   52693
Total:          2 2173 6017.5     16   53703

Percentage of the requests served within a certain time (ms)
  50%     16
  66%     23
  75%     47
  80%   2660
  90%   7631
  95%  14241
  98%  27471
  99%  28506
 100%  53703 (longest request)
```
