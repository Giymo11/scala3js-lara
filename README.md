
# TODO

- automatically generate the facade libraries


# tech used

- mill
- scala 3
- scalajs
- scalably-typed

- zio http
- tapir
- sttp
- airstream
- laminar

- uuid

# Building & Running

After adding an npm-dependency, execute `mill lara.frontend.compile` and `stc -d ./out/lara/frontend/webpackOutputPath/dest <lib>` to generate the local facade library. 


# Troubleshooting

If you get something like 
```
java.io.IOException: GetOverlappedResult() failed for read operation: 233
```
it may have to do with your environment variables for things like JDK_HOME being on different drives on Windows.

Try moving the project to the C drive.
