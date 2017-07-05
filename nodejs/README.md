# Test Environment

CPU: Intel(R) Core(TM) i7-3630QM CPU @ 2.40GHz
RAM Total: 24578628k

# Test Methodology

Under `ulimit -n 20000`, running the server using

```
/usr/bin/time node ./http_server.js
```

with Node.js version

```
v8.1.3
```

Test using:

```
ab -c 10000 -n 20000 http://127.0.0.1:8080/perf
```

The above command is run once to prime the Node.js program (JIT etc). The second
run of the command is used to produce the test results. Note: memory
consumption during the first run still counts towards maxresident.

# Test Results

Note: the results are highly inconsistent from run to run. The first run (used
to prime Node.js) failed with the following error:

```
apr_pollset_poll: The timeout specified has expired (70007)
Total of 19238 requests completed
```

`ab` outputs:

```
This is ApacheBench, Version 2.3 <$Revision: 1796539 $>
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
Time taken for tests:   8.124 seconds
Complete requests:      20000
Failed requests:        0
Total transferred:      3180000 bytes
HTML transferred:       1680000 bytes
Requests per second:    2461.69 [#/sec] (mean)
Time per request:       4062.249 [ms] (mean)
Time per request:       0.406 [ms] (mean, across all concurrent requests)
Transfer rate:          382.24 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0  311 447.2      2    1042
Processing:    10  314 792.2     29    7014
Waiting:       10  314 792.2     29    7014
Total:         11  625 1082.9     39    8030

Percentage of the requests served within a certain time (ms)
  50%     39
  66%    361
  75%   1039
  80%   1254
  90%   1857
  95%   2688
  98%   4420
  99%   4481
 100%   8030 (longest request)
```

`time` outputs:

```
4.33user 1.44system ...elapsed ...CPU (0avgtext+0avgdata 87896maxresident)k
0inputs+0outputs (0major+38922minor)pagefaults 0swaps
```
