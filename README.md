
If you get something like 
```
java.io.IOException: GetOverlappedResult() failed for read operation: 233
```
it may have to do with your environment variables for things like JDK_HOME being on different drives on Windows.

Try moving the project to the C drive.



# tech used

- mill
- scala 3
- scalajs
- zio http
- tapir
- laminar

